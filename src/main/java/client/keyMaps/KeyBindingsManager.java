package client.keyMaps;

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

/**
 * Manages the persistence of key bindings.
 * <p>
 * This class is responsible for saving the current key configuration to an XML file
 * and loading it back when the application starts. It also handles the generation
 * of the DTD for validation.
 * </p>
 */
public class KeyBindingsManager {

    // Define the folder and file paths
    // We use a relative path that works when running the project from the root folder
    private static final String CONFIG_FOLDER = "TetrisDistribuidos/";
    private static final String FILE_NAME = "keybindings.xml";
    private static final String FILE_PATH = CONFIG_FOLDER + FILE_NAME;
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


    /**
     * Saves the current bindings from the provided handler to an XML file.
     * <p>
     * It ensures that the configuration directory and DTD file exist before writing.
     * </p>
     *
     * @param handler The {@link KeyInputHandler} containing the current key mappings to be saved.
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
            for (InputAction action : InputAction.values()) {
                int key = handler.getKeyForAction(action, true);
                if (key != -1) {
                    addBindingElement(doc, p1Element, action.name(), key);
                }
            }

            // --- PLAYER 2 SETTINGS ---
            Element p2Element = doc.createElement(PLAYER_2_TAG);
            rootElement.appendChild(p2Element);
            for (InputAction action : InputAction.values()) {
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
            StreamResult result = new StreamResult(new File(FILE_PATH));

            transformer.transform(source, result);

        } catch (Exception e) {
            // Error handling silently ignored as requested, or you can log to a file
        }
    }

    /**
     * Loads bindings from the XML file into the provided handler with DTD VALIDATION.
     *
     * @param handler The {@link KeyInputHandler} where the loaded bindings will be applied.
     * @return {@code true} if the bindings were loaded successfully, {@code false} otherwise (e.g., file not found or parsing error).
     */
    public static boolean loadBindings(KeyInputHandler handler) {
        File xmlFile = new File(FILE_PATH);
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
                loadBindingsFromElement((Element) globalList.item(0), handler, 0);
            }

            // Load Player 1
            NodeList p1List = doc.getElementsByTagName(PLAYER_1_TAG);
            if (p1List.getLength() > 0) {
                loadBindingsFromElement((Element) p1List.item(0), handler, 1);
            }

            // Load Player 2
            NodeList p2List = doc.getElementsByTagName(PLAYER_2_TAG);
            if (p2List.getLength() > 0) {
                loadBindingsFromElement((Element) p2List.item(0), handler, 2);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ensures that the configuration directory exists. Creates it if it does not.
     */
    private static void ensureConfigFolderExists() {
        File folder = new File(CONFIG_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }

    /**
     * Ensures that the DTD file exists. Creates it if it does not to allow XML validation.
     */
    private static void ensureDTDExists() {
        File dtdFile = new File(DTD_FILE_PATH);
        if (!dtdFile.exists()) {
            try (FileWriter writer = new FileWriter(dtdFile)) {
                writer.write("""
                        <!ELEMENT KeyBindings (Global, Player1, Player2)>
                        <!ELEMENT Global (Bind*)>
                        <!ELEMENT Player1 (Bind*)>
                        <!ELEMENT Player2 (Bind*)>
                        <!ELEMENT Bind EMPTY>
                        <!ATTLIST Bind\s
                            action CDATA #REQUIRED
                            keyCode CDATA #REQUIRED
                        >""");
            }
            catch (IOException e) { System.out.println("FATAL ERROR while trying to create the DTD"); } // This should never happen, if it does your computer is broken sry
        }
    }

    /**
     * Helper method to add a single binding element to the XML document.
     *
     * @param doc        The XML Document.
     * @param parent     The parent element (e.g., Player1).
     * @param actionName The name of the action.
     * @param keyCode    The key code associated with the action.
     */
    private static void addBindingElement(Document doc, Element parent, String actionName, int keyCode) {
        Element bind = doc.createElement(BINDING_TAG);
        bind.setAttribute(ATTR_ACTION, actionName);
        bind.setAttribute(ATTR_KEY, String.valueOf(keyCode));
        parent.appendChild(bind);
    }

    /**
     * Helper method to parse bindings from an XML element and apply them to the handler.
     *
     * @param parent     The parent XML element containing "Bind" tags.
     * @param handler    The input handler to update.
     * @param actionType 0 for Global, 1 for Player 1, 2 for Player 2.
     */
    private static void loadBindingsFromElement(Element parent, KeyInputHandler handler, int actionType) {
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

                    if (actionType == 0) {
                        if (action == InputAction.GO_BACK_TO_START_MENU)
                            handler.bindGoBackToMenuKey(keyCode);
                    }
                    else handler.changeKeyBinding(action, keyCode, actionType == 1);
                }
                catch (NumberFormatException ex) { System.out.println("There was a non valid key value in the xml, the binding will be ignored"); }
                catch (IllegalArgumentException iae) { System.out.println("There was a non valid action value in the xml, the binding will be ignored"); }
            }
        }
    }
}