package tetris.keyMaps;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KeyBindingsManager {

    // Define the folder and file paths
    // We use a relative path that works when running the project from the root folder
    private static final String CONFIG_FOLDER = "src/main/resources/config/";
    private static final String FILE_NAME = CONFIG_FOLDER + "keybindings.xml";
    private static final String DTD_FILE_NAME = "keybindings.dtd"; // Keeps the DTD reference simple in XML
    private static final String DTD_FILE_PATH = CONFIG_FOLDER + DTD_FILE_NAME;

    // XML Tags
    private static final String ROOT_TAG = "KeyBindings";
    private static final String GLOBAL_TAG = "Global";
    private static final String PLAYER_1_TAG = "Player1";
    private static final String PLAYER_2_TAG = "Player2";
    private static final String BINDING_TAG = "Bind";
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_KEY = "keyCode";

    // DTD Content
    private static final String DTD_CONTENT =
            "<!ELEMENT KeyBindings (Global, Player1, Player2)>\n" +
                    "<!ELEMENT Global (Bind*)>\n" +
                    "<!ELEMENT Player1 (Bind*)>\n" +
                    "<!ELEMENT Player2 (Bind*)>\n" +
                    "<!ELEMENT Bind EMPTY>\n" +
                    "<!ATTLIST Bind \n" +
                    "    action CDATA #REQUIRED\n" +
                    "    keyCode CDATA #REQUIRED\n" +
                    ">";

    private static final InputAction[] PLAYER_ACTIONS = {
            InputAction.MOVE_LEFT, InputAction.MOVE_RIGHT,
            InputAction.SOFT_DROP, InputAction.HARD_DROP,
            InputAction.ROTATE_LEFT, InputAction.ROTATE_RIGHT,
            InputAction.FLIP, InputAction.HOLD
    };

    /**
     * Saves the current bindings from the handler to an XML file.
     */
    public static void saveBindings(KeyInputHandler handler) {
        // Ensure the folder and DTD exist
        ensureConfigFolderExists();
        ensureDTDExists();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(ROOT_TAG);
            doc.appendChild(rootElement);

            // --- GLOBAL SETTINGS ---
            Element globalElement = doc.createElement(GLOBAL_TAG);
            rootElement.appendChild(globalElement);
            addBindingElement(doc, globalElement, InputAction.GO_BACK_TO_START_MENU.name(), handler.getGoBackToMenuKey());

            // --- PLAYER 1 SETTINGS ---
            Element p1Element = doc.createElement(PLAYER_1_TAG);
            rootElement.appendChild(p1Element);
            for (InputAction action : PLAYER_ACTIONS) {
                int key = handler.getKeyForAction(action, true);
                if (key != -1) {
                    addBindingElement(doc, p1Element, action.name(), key);
                }
            }

            // --- PLAYER 2 SETTINGS ---
            Element p2Element = doc.createElement(PLAYER_2_TAG);
            rootElement.appendChild(p2Element);
            for (InputAction action : PLAYER_ACTIONS) {
                int key = handler.getKeyForAction(action, false);
                if (key != -1) {
                    addBindingElement(doc, p2Element, action.name(), key);
                }
            }

            // Write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Format the XML nicely
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Add DOCTYPE declaration (System ID only, expecting DTD in same folder)
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD_FILE_NAME);

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_NAME));

            transformer.transform(source, result);

        } catch (Exception e) {
            // Error handling silently ignored as requested, or you can log to a file
        }
    }

    /**
     * Loads bindings from the XML file with DTD VALIDATION.
     */
    public static boolean loadBindings(KeyInputHandler handler) {
        File xmlFile = new File(FILE_NAME);
        if (!xmlFile.exists()) {
            return false;
        }

        // Ensure DTD exists so validation doesn't fail due to missing file
        ensureConfigFolderExists();
        ensureDTDExists();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            // Enable Validation
            dbFactory.setValidating(true);
            dbFactory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Set Error Handler to capture validation errors (silent)
            dBuilder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) { }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception; // Stop parsing on error
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception; // Stop parsing on fatal error
                }
            });

            // Parse (triggers validation)
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // --- Parse Data ---

            // Load Global
            NodeList globalList = doc.getElementsByTagName(GLOBAL_TAG);
            if (globalList.getLength() > 0) {
                loadBindingsFromElement((Element) globalList.item(0), handler, null);
            }

            // Load Player 1
            NodeList p1List = doc.getElementsByTagName(PLAYER_1_TAG);
            if (p1List.getLength() > 0) {
                loadBindingsFromElement((Element) p1List.item(0), handler, true);
            }

            // Load Player 2
            NodeList p2List = doc.getElementsByTagName(PLAYER_2_TAG);
            if (p2List.getLength() > 0) {
                loadBindingsFromElement((Element) p2List.item(0), handler, false);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private static void ensureConfigFolderExists() {
        File folder = new File(CONFIG_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private static void ensureDTDExists() {
        File dtdFile = new File(DTD_FILE_PATH);
        if (!dtdFile.exists()) {
            try (FileWriter writer = new FileWriter(dtdFile)) {
                writer.write(DTD_CONTENT);
            } catch (IOException e) {
                // Silent catch
            }
        }
    }

    private static void addBindingElement(Document doc, Element parent, String actionName, int keyCode) {
        Element bind = doc.createElement(BINDING_TAG);
        bind.setAttribute(ATTR_ACTION, actionName);
        bind.setAttribute(ATTR_KEY, String.valueOf(keyCode));
        parent.appendChild(bind);
    }

    private static void loadBindingsFromElement(Element parent, KeyInputHandler handler, Boolean isPlayer1) {
        NodeList bindings = parent.getElementsByTagName(BINDING_TAG);

        for (int i = 0; i < bindings.getLength(); i++) {
            Node node = bindings.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String actionName = elem.getAttribute(ATTR_ACTION);
                String keyStr = elem.getAttribute(ATTR_KEY);

                try {
                    int keyCode = Integer.parseInt(keyStr);
                    InputAction action = InputAction.valueOf(actionName);

                    if (isPlayer1 == null) {
                        if (action == InputAction.GO_BACK_TO_START_MENU) {
                            handler.bindGoBackToMenuKey(keyCode);
                        }
                    } else {
                        handler.changeKeyBinding(action, keyCode, isPlayer1);
                    }
                } catch (IllegalArgumentException ex) {
                    // Ignore invalid bindings
                }
            }
        }
    }
}