package com.infostretch.labs.plugins;

import com.infostretch.labs.transformers.Transformer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.List;
import java.util.ArrayList;

/**
 * Handles transformation of SecretBuildWrapper Plugin properties
 *
 * @author ...
 */
public class SecretBuildWrapper extends Plugins {
    static final String credentialsbindingimpl = "org.jenkinsci.plugins.credentialsbinding.impl.";
    static final String dockerbindingimpl = "org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentialsBinding";

    public SecretBuildWrapper(Transformer transformer, Node node) {
        super(transformer, node);
    }
 @Override
    public void transformBuild() {
        List<String> creds = new ArrayList<String>();
        NodeList bindings = getElementByTag("bindings").getChildNodes();
       for (int i=0; i < bindings.getLength(); i++) {
            Node binding = bindings.item(i);
            String nodeName = binding.getNodeName();
            String converted;
            if (nodeName.startsWith(credentialsbindingimpl)) {
                String impl = nodeName.substring(credentialsbindingimpl.length());
                switch (impl) {
                   case "StringBinding":
                        converted = "string(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", variable: " + getElementByTag(binding, "variable").getTextContent() +
                                    ")";
                        break;
                    case "UsernamePasswordBinding":
                        converted = "usernameColonPassword(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", variable: " + getElementByTag(binding, "variable").getTextContent() +
                                    ")";
                        break;
                    case "FileBinding":
                        converted = "file(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", variable: " + getElementByTag(binding, "variable").getTextContent() +
                                    ")";
                        break;
                    case "ZipFileBinding":
                        converted = "zip(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", variable: " + getElementByTag(binding, "variable").getTextContent() +
                                    ")";
                        break;
                    case "SSHUserPrivateKeyBinding":
                        converted = "sshUserPrivateKey(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", keyFileVariable: " + getElementByTag(binding, "keyFileVariable").getTextContent() +
                                    ", passphraseVariable: " + getElementByTag(binding, "passphraseVariable").getTextContent() +
                                    ", usernameVariable: " + getElementByTag(binding, "usernameVariable").getTextContent() +
                                    ")";
                        break;
                    case "UsernamePasswordMultiBinding":
                        converted = "usernamePassword(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", passwordVariable: " + getElementByTag(binding, "passwordVariable").getTextContent() +
                                    ", usernameVariable: " + getElementByTag(binding, "usernameVariable").getTextContent() +
                                    ")";
                        break;
                    case "CertificateMultiBinding":
                       converted = "certificate(" +
                                      "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                                    ", aliasVariable: " + getElementByTag(binding, "aliasVariable").getTextContent() +
                                    ", keystoreVariable: " + getElementByTag(binding, "keystoreVariable").getTextContent() +
                                    ", passwordVariable: " + getElementByTag(binding, "passwordVariable").getTextContent() +
                                    ")";
                        break;
                    default:
                        converted = "/* unhandled basic type: " + impl + "*/";
                }
            } else if (nodeName.equals(dockerbindingimpl)) {
                converted = "dockerCert(" +
                              "credentialsId: " + getElementByTag(binding, "credentialsId").getTextContent() +
                            ", variable: " + getElementByTag(binding, "variable").getTextContent() +
                            ")";
            } else {
                converted = "/* unhandled type: " + nodeName + "*/";
            }
            creds.add(converted);
        }

        appendBuildSteps("\t\t// SecretBuildWrapper -- some assembly required");
        appendBuildSteps("\nwithCredentials([" + creds.toString() +
            "]) {\n" +
            "\t\t\t// please move build block here\n" +
            "}\n");
    }
}