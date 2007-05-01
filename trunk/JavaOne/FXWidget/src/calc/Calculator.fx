package calc;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.text.DecimalFormat;
import java.lang.System;
import java.awt.Container;
import java.awt.BorderLayout;

class CalcButton extends CompositeNode {
    attribute pressedImage: Image;
    attribute image: Image;
    attribute pressed: Boolean;
    attribute action: function();
}

function CalcButton.composeNode() =
ImageView {
    onMousePressed: operation(e) {pressed = true;}
    onMouseReleased: operation(e) {
        pressed = false; 
        if (hover) {
            (this.action)();
        }
    }
    var pressHover = bind hover and pressed
    image: bind if pressHover then pressedImage else image
};

attribute CalcButton.isSelectionRoot = true;

public class Calculator extends CompositeNode {
    attribute reg1: Number;
    attribute reg2: Number;
    attribute mem: Number;
    attribute operator: String;
    attribute isFixReg: Boolean;
    attribute text: String;
    operation calculate(op:String, r1:Number, r2:Number):Number;
    operation input(value:String);
    
}

function Calculator.calculate(op, r1, r2) =

    if (op == "+")
    then r1 + r2
    else if (op == "-") 
    then r1 - r2
    else if (op == "*")
    then r1 * r2
    else if (op == "/")
    then r1 / r2
    else r2
;

operation Calculator.input(value) {
    function formatNum(n) = n format as <<###,###,###,###.########>>;
    if (value == "decimal") {
        value = ".";
    }
    if (value == "c") {
        reg1 = 0;
        reg2 = 0;
        operator = "";
        text = "0";
        isFixReg = true;
    } else if (value == "del") {
        if (text.length() > 1) {
            text = text.substring(0, text.length()-1);
        } else {
            text = "0";
            isFixReg = true;
        }
    } else if (value in (select "{i}" from i in [0..9]) or value == ".") {
        if (isFixReg) {
            if (value == ".") {
                text = "0.";
            } else {
                text = "{value}";
            }
        } else if (value <> "." or text.indexOf(".") < 0) {
            text = "{text}{value}";
            
        }
        isFixReg = false;
        
    } else if (value == "+" or value == "-" or value == "*" or value == "/" or value == "=") {
        var num = new DecimalFormat("###,###,###,###.#######").parse(text);
        reg2 = num;
        reg1 = calculate(operator, reg1, reg2);
        operator = value;
        text = formatNum(reg1);
        isFixReg = true;
    } else if (value == "mr") {
        text = formatNum(mem);
        operator = "";
        isFixReg = true;
    } else if (value == "m+") {
        reg1 = calculate(operator, reg1, reg2);
        text = formatNum(reg1);
        mem = mem + reg1;
        operator = "";
        isFixReg = true;
    } else if (value == "m-") {
        reg1 = calculate(operator, reg1, reg2);
        text = formatNum(reg1);
        mem = mem - reg1;
        operator = "";
        isFixReg = true;
    } else if (value == "mc") {
        mem = 0;
    }
    
}

attribute Calculator.text = "0";
attribute Calculator.isFixReg = true;
attribute Calculator.focusable = true;
attribute Calculator.onKeyTyped = operation(e:KeyEvent) {
    input(e.keyChar);
};

attribute Calculator.onKeyDown = operation(e:KeyEvent) {
    if (e.keyStroke == ENTER:KeyStroke) {
        input("=");
    } else if (e.keyStroke == BACK_SPACE:KeyStroke) {        
        input("del");
    }
};

function Calculator.composeNode() =

Group {
    var ops = ["/", "*", "-", "+"]
    content:
    [ImageView {
        image: {url: "{__DIR__}/images/Calculator.png"}
    },
    ImageView { 
        transform: translate(13, 8)
        image: {url: "{__DIR__}/images/lcd-backlight.png"}
    },
    Text {
        transform: translate(150, 18)
        halign: TRAILING
        font: Font {face: ARIAL size: 20}
        content: bind text
        fill: new Color(0, 0, 0, 0.4)
    },
    foreach (i in [1,2,3])
    CalcButton {
        transform: translate(16+indexof i *38, 153)
        image: {url: "{__DIR__}/images/{i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input("{i}");}
    },
    foreach (i in [4,5,6])
    CalcButton {
        transform: translate(16+indexof i *38, 153-38)
        image: {url: "{__DIR__}/images/{i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input("{i}");}    
    },
    foreach (i in [7,8,9])
    CalcButton {
        transform: translate(16+indexof i *38, 153-38*2)
        image: {url: "{__DIR__}/images/{i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input("{i}");}
    },
    foreach (i in ["0", "decimal", "c"])
    CalcButton {
        transform: translate(16+indexof i * 38, 191)
        image: {url: "{__DIR__}/images/{i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input(i);}    
    },
    foreach (i in ["m+", "m-", "mc", "mr"])
    CalcButton {
        transform: translate(16+indexof i * 28, 52)
        image: {url: "{__DIR__}/images/{i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input(i);}
    },
    foreach (i in ["div", "mult", "sub", "add"])
    CalcButton {
        transform: translate(130 + (if indexof i > 0 then 1 else 0), 52+27*indexof i)
        image: {url: bind "{__DIR__}/images/{if operator == ops[indexof i] then "a{i}" else i}.png"}
        pressedImage: {url: "{__DIR__}/images/d{i}.png"}
        action: operation() {input(ops[indexof i]);}
    },
    CalcButton {
        transform: translate(131, 170)
        image: {url: "{__DIR__}/images/equal.png"}
        pressedImage: {url: "{__DIR__}/images/dequal.png"}
        action: operation() {input("=");}    
    }]
};

var canvas = Canvas {
    background: new Color(0, 0, 0, 0)
    content: Calculator {
        focused: true
    }
};

GLOSSITOPE:Container.add(canvas.getComponent());


