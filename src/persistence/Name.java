package persistence;

import javax.xml.bind.annotation.XmlElement;

public class Name {

String text;

@XmlElement
public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}
}