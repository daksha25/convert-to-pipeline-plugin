package com.infostretch.labs.plugins;

import com.infostretch.labs.transformers.PublisherTransformer;
import com.infostretch.labs.transformers.Transformer;
import org.w3c.dom.Node;

/**
 * Handles transformation of ExtendedEmailPublisher Plugin properties
 *
 * @author ...
 */
public class ExtendedEmailPublisher extends Plugins {

    public ExtendedEmailPublisher(Transformer transformer, Node node) {
        super(transformer, node);
    }

    @Override
    public void transformPublisher() {
        transformer.setOnlyBuildTrigger(false);
        appendPublishSteps("\n\t\t// ExtendedEmailPublisher notification");
        appendPublishSteps("\n\t\tstep([$class: 'emailext'" +
            ", attachLog: " + Boolean.valueOf(getElementByTag("attachBuildLog").getTextContent()) +
            ", attachmentsPattern: " + getElementByTag("attachmentsPattern").getTextContent() +
            ", defaultContent: " + getElementByTag("body").getTextContent() +
            ", compressLog: " + Boolean.valueOf(getElementByTag("compressBuildLog").getTextContent()) +
            ", mimeType: " + getElementByTag("contentType").getTextContent() +
            ", postsendScript: " + getElementByTag("postsendScript").getTextContent() +
            ", presendScript: " + getElementByTag("presendScript").getTextContent() +
            ", replyTo: " + getElementByTag("replyTo").getTextContent() +
            ", subject: " + getElementByTag("subject").getTextContent() +
            ", to: " + getElementByTag("recipientList").getTextContent() +
           "])\n");
    }
}