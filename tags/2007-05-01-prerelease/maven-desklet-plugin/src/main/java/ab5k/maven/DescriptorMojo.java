package ab5k.maven;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Repository;
import org.apache.maven.project.MavenProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Creates a desklet descriptor from the project file.
 *
 * @goal buildDescriptor
 *
 * @phase compile
 */
public class DescriptorMojo
        extends AbstractMojo {
    
    /**
     * Name of desklet class
     * @parameter
     * @required
     */
    private String deskletClass;
    
    /**
     * Location of the file.
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    
    /**
     * Project instance, used to add new source directory to the build.
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    
    public void execute()
    throws MojoExecutionException {
        File f = outputDirectory;
        File metaInf = new File( f, "classes"+File.separator+"META-INF");
        if ( !metaInf.exists() ) {
            metaInf.mkdirs();
        }
        Document doc = new Document();
        Element root = new Element("desklet");
        root.setAttribute("version", "0.1");
        doc.setRootElement( root );
        
        Element dclass = new Element("class");
        dclass.setText( this.deskletClass );
        root.addContent( dclass );
        
        Element dversion = new Element("version");
        dversion.setText( project.getVersion() );
        root.addContent( dversion );
        
        Element name = new Element("name");
        name.setText( project.getName() );
        root.addContent( name );
        
        Element description = new Element("description");
        description.setText( project.getDescription() );
        root.addContent( description );
        
        if( project.getContributors() != null && project.getContributors().size() > 0){
            Contributor c= (Contributor) project.getContributors().iterator().next();
            Element author = new Element("author");
            author.setText( c.getName() );
            root.addContent( author );
        }
        if( project.getUrl() != null ){
            Element homepage = new Element("homepage");
            homepage.setText( project.getUrl() );
            root.addContent( homepage );
        }
        
        List repos = project.getRepositories();
        for( int i=0; repos != null && i < repos.size(); i++ ){
            Repository repo = (Repository) repos.get(i);
            Element repository = new Element( "repository" );
            repository.setText( repo.getUrl() );
            if( repo.getLayout() == null || !repo.getLayout().equals("legacy") ){
                repository.setAttribute("layout", "maven-2");
            }
            root.addContent( repository );
        }
        List deps = project.getDependencies();
        for( int i=0; deps != null && i < deps.size(); i++ ){
            Dependency dep = (Dependency) deps.get(i);
            if( dep.getScope() != null &&
                    !dep.getScope().equals("test") &&
                    !dep.getScope().equals("provided") ){
                Element dependency = new Element("dependency");
                Element groupId = new Element("groupId");
                groupId.setText( dep.getGroupId() );
                dependency.addContent( groupId );
                
                Element artifactId = new Element("artifactId");
                artifactId.setText( dep.getArtifactId() );
                dependency.addContent( artifactId );
                
                Element version = new Element("version");
                version.setText( dep.getVersion() );
                dependency.addContent( version );
                
                if( dep.getType() != null ){
                    Element type = new Element("type");
                    type.setText( dep.getType() );
                    dependency.addContent( type );
                }
                root.addContent( dependency );
            }
        }
        File desc = new File( metaInf, "desklet.xml");
        try{
            
            FileWriter writer = new FileWriter( desc );
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            out.output(doc, writer);
            writer.flush();
            writer.close();
        } catch(IOException ioe){
            throw new MojoExecutionException("Error writing desklet descriptor: "+ desc.getAbsolutePath(), ioe );
        }
    }
}
