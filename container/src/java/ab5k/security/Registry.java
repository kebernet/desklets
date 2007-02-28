/*
 * Registry.java
 *
 * Created on August 3, 2006, 4:47 PM
 *
 */
package ab5k.security;

import com.totsp.util.BeanArrayList;
import com.totsp.util.SimpleUUIDGen;
import com.totsp.util.StreamUtility;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.security.AccessController;
import java.security.PrivilegedAction;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joshy.util.u;


/**
 *
 * @author cooper
 */
public class Registry {
    private static final Logger LOG = Logger.getLogger("AB5K");
    private static final Properties TYPE_TO_DIRECTORY = new Properties();

    static {
        TYPE_TO_DIRECTORY.put("jar", "jars/");
        TYPE_TO_DIRECTORY.put("tld", "tlds/");
        TYPE_TO_DIRECTORY.put("file", "files/");
        TYPE_TO_DIRECTORY.put("war", "wars/");
    }

    private static final String PROTOCOL = "desklet";
    private static final SimpleUUIDGen UUID = new SimpleUUIDGen();
    private static final DeskletAdministrationPermission PERMISSION = new DeskletAdministrationPermission("Desklet Registry",
            "all");
    public static final File HOME = new File(System.getProperty("user.home") +
            File.separator + ".ab5k");
    public static final File REPO = new File( HOME, "repository");
    private static final Registry INSTANCE = new Registry();
    private BeanArrayList<DeskletConfig> deskletConfigs = new BeanArrayList<DeskletConfig>("deskletConfigs",
            this);

    /** Creates a new instance of Registry */
    private Registry() {
        u.p("registry created");
        try {
            if (!HOME.exists()) {
                if (!HOME.mkdirs()) {
                    throw new Exception(
                            "Unable to make ab5k configuration directory.");
                }
            }
            
            if (!REPO.exists()) {
                if (!REPO.mkdirs()) {
                    throw new Exception(
                            "Unable to make ab5k repository directory.");
                }
            }
            
            final File[] inHome = HOME.listFiles();

            for (File checkDesklet : inHome) {
                boolean isDesklet = false;
                File[] search = checkDesklet.listFiles();

                for (int i = 0;
                (search != null) && (i < search.length) && !isDesklet;
                i++)
                    if (search[i].getName().equals("META-INF")) {
                    isDesklet = true;
                    }

                if (isDesklet) {
                    try {
                        readDeskletConfig(checkDesklet);
                    } catch (Exception e) {
                        LOG.log(Level.WARNING,
                                "Unable to read desklet: " +
                                checkDesklet.getName(), e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    "Critical Exception loading desklet registry!", e);
        }
    }

    DeskletConfig readDeskletConfig(File file)
    throws IOException, JDOMException {
        SecurityManager sm = System.getSecurityManager();
        Object ctx = sm.getSecurityContext();
        sm.checkPermission(Registry.PERMISSION, ctx);

        final DeskletConfig config = new DeskletConfig();
        config.setHomeDir(file);
        config.setUUID(file.getName());

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new File(file,
                "META-INF" + File.separator + "desklet.xml"));
        Element root = doc.getRootElement();
        String name = root.getChildTextTrim("name");
        config.setName(name);
        config.setVersion(root.getAttributeValue("version"));

        if ((config.getName() == null) || (config.getVersion() == null)) {
            throw new RuntimeException(
                    "A name and version are required for desklet " +
                    file.getName());
        }

        String className = root.getChildTextTrim("class");
        config.setClassName(className);

        List<Element> depNodes = (List<Element>) root.getChildren("dependency");
        ArrayList<Dependency> deps = new ArrayList<Dependency>();

        for (int i = 0; i < depNodes.size(); i++) {
            try {
                Element depNode = (Element) depNodes.get(i);
                String groupId = depNode.getChildTextTrim("groupId");
                String artifactId = depNode.getChildTextTrim("artifactId");
                String version = depNode.getChildTextTrim("version");
                String type = depNode.getChildTextTrim("type");
                deps.add(new Dependency(groupId, artifactId, version, type));
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception reading dependency.", e);
            }
        }

        config.setDependencies(deps.toArray(new Dependency[deps.size()]));

        List<Element> repoNodes = (List<Element>) root.getChildren("repository");
        ArrayList<URL> repos = new ArrayList<URL>();
        repos.add(new URL("http://ibiblio.org/maven/"));

        for (int i = 0; (repoNodes != null) && (i < repoNodes.size()); i++) {
            try {
                Element repoNode = repoNodes.get(i);
                repos.add(new URL(repoNode.getTextTrim()));
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Exception reading repository URL.", e);
            }
        }

        config.setRepositories(repos.toArray(new URL[repos.size()]));

        config.setAuthorName(root.getChildText("author"));
        config.setHomePage((root.getChildText("homepage") != null)
        ? new URL(root.getChildTextTrim("homepage")) : null);
        config.setVersion(root.getAttributeValue("version"));
        config.setDescription(root.getChildText("description"));

        try {
            ClassLoader loader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    try {
                        return new ClassLoader(config.getUUID(),
                                config.getName(), getDependencies(config));
                    } catch (IOException e) {
                        e.printStackTrace();

                        return null;
                    }
                }
            });

            config.setClassLoader(loader);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        this.deskletConfigs.add(config);

        return config;
    }

    public static Registry getInstance() {
        SecurityManager sm = System.getSecurityManager();
        Object ctx = sm.getSecurityContext();
        sm.checkPermission(PERMISSION, ctx);

        return Registry.INSTANCE;
    }

    URL[] getDependencies(DeskletConfig config) throws IOException {
        ArrayList<URL> urls = new ArrayList<URL>();
        urls.add(new File(HOME, config.getUUID()).toURI().toURL());

        File common = new File(HOME, "common" + File.separator + "lib");
        common.mkdirs();

        File[] commonLibs = common.listFiles();

        

        File libs = new File(config.getHomeDir(),
                "META-INF" + File.separator + "lib");
        libs.mkdirs();

        for (Dependency dep : config.getDependencies()) {
            File libsDest = new File(libs,
                    dep.getGroupId() + File.separator +
                    TYPE_TO_DIRECTORY.getProperty(dep.getType(), "./"));
            
            File libArtifact = new File(libsDest,
                    dep.getArtifactId() + "-" + dep.getVersion() + "." +
                    dep.getType());

            if( libArtifact.exists() ){
                 LOG.log( Level.INFO , "Depedency found in desklet lib: "+ libArtifact.getAbsolutePath() );
                 urls.add(libArtifact.toURI().toURL());
                 continue;
            }
            
            File repoDest = new File(REPO,
                    dep.getGroupId() + File.separator +
                    TYPE_TO_DIRECTORY.getProperty(dep.getType(), "./"));
            repoDest.mkdirs();
            File artifact = new File(repoDest,
                    dep.getArtifactId() + "-" + dep.getVersion() + "." +
                    dep.getType());
            LOG.log( Level.INFO, "Dependency needed in repository: "+ artifact.getAbsolutePath() );
            if (!artifact.exists()) {
                boolean downloaded = false;
                LOG.log( Level.INFO, "Downloading: "+ artifact.getAbsolutePath());
                for (int i = 0;
                !downloaded && (i < config.getRepositories().length);
                i++) {
                    URL repo = config.getRepositories()[i];

                    try {
                        URL source = new URL(repo,
                                dep.getGroupId() + "/" +
                                TYPE_TO_DIRECTORY.getProperty(dep.getType(), "") +
                                dep.getArtifactId() + "-" + dep.getVersion() +
                                "." + dep.getType());
                        StreamUtility.copyStream(source.openStream(),
                                new FileOutputStream(artifact));
                        downloaded = true;
                    } catch (Exception e) {
                        LOG.log(Level.FINEST,
                                "Unable to get " + dep.getArtifactId() + "-" +
                                dep.getVersion() + " from " + repo, e);
                    }
                }
            }

            urls.add(artifact.toURI().toURL());
        }

        for (File commonLib : commonLibs) {
            urls.add(commonLib.toURI().toURL());
        }
        return urls.toArray(new URL[urls.size()]);
    }

    public String getDeskletName(String uuid) {
        DeskletConfig result = getDeskletConfig(uuid);

        return (result == null) ? null : result.getName();
    }

    public DeskletConfig getDeskletConfig(String uuid) {
        if (!(new File(HOME, uuid).exists())) {
            return null;
        }

        for (DeskletConfig config : this.deskletConfigs) {
            if (config.getUUID().equals(uuid)) {
                return config;
            }
        }

        return null;
    }

    public DeskletConfig installDesklet(URL installFrom)
    throws MalformedURLException, IOException, JDOMException {
        SecurityManager sm = System.getSecurityManager();
        Object ctx = sm.getSecurityContext();
        sm.checkPermission(PERMISSION, ctx);

        URL url = installFrom;

        if (url.getProtocol().equals(PROTOCOL)) {
            String ext = installFrom.toExternalForm();
            url = new URL("http" +
                    ext.substring(PROTOCOL.length(), ext.length()));
        }

        String uuid = UUID.nextUUID();
        File file = new File(HOME, uuid + ".jar");

        while (file.exists()) {
            file = new File(HOME, uuid + ".jar");
        }

        StreamUtility.copyStream(url.openStream(), new FileOutputStream(file));

        JarFile jar = new JarFile(file);
        File destination = new File(HOME, uuid);
        destination.mkdirs();

        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            LOG.log(Level.FINE, "Writing: " + entry.getName());

            if (entry.isDirectory()) {
                new File(destination, entry.getName()).mkdirs();
            } else {
                StreamUtility.copyStream(jar.getInputStream(entry),
                        new FileOutputStream(new File(destination, entry.getName())));
            }
        }

        DeskletConfig cfg = this.readDeskletConfig(destination);
        return cfg;
    }

    public void uninstallDesklet(String uuid) {
        DeskletConfig config = this.getDeskletConfig(uuid);

        if (config != null) {
            uninstallDesklet(config);
        }
    }

    public void uninstallDesklet(DeskletConfig config) {
        SecurityManager sm = System.getSecurityManager();
        Object ctx = sm.getSecurityContext();
        sm.checkPermission(PERMISSION, ctx);

        DeskletManager manager = DeskletManager.getInstance();

        try {
            manager.shutdownDesklet(config.getUUID());
        } catch (Exception e) {
            LOG.log(Level.INFO, "Exception on shutdown before uninstall.", e);
        }

        File jar = new File(HOME, config.getUUID() + ".jar");
        jar.delete();
        this.deskletConfigs.remove(config);
        recursiveDelete(config.getHomeDir());
    }

    public static void recursiveDelete(File f) {
        if (f.isDirectory()) {
            File[] children = f.listFiles();

            if (children != null) {
                for (File child : children) {
                    recursiveDelete(child);
                }
            }
        }

        f.delete();
    }

    public BeanArrayList<DeskletConfig> getDeskletConfigs() {
        return this.deskletConfigs;
    }
}
