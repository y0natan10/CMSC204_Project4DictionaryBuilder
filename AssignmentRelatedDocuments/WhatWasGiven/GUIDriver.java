import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * ----------------------------------------------------------------------
 * File: GUIDriver.java
 * Author: Montgomery College CMSC204 Staff
 * Course: CMSC204 - Computer Science II
 * Project: DictionaryBuilder
 * Institution: Montgomery College
 * Year: 2025
 *
 * Description:
 *     A simple graphical interface driver provided to help students interact
 *     with their DictionaryBuilder project. Supports adding, deleting,
 *     and searching words as well as viewing stats in a GUI.
 *
 * Notes:
 *     This file is safe to distribute to students and may be used for manual
 *     testing or demonstration of project functionality.
 *
 * License:
 *     This file is provided for educational use in CMSC204 at Montgomery College.
 *     Redistribution outside this course is not permitted.
 * ----------------------------------------------------------------------
 */

/**
 * Project 4 Swing Application (GUI)
 * Mirrors the CLI driver (add / remove|delete / search / list / quit).
 *
 * Requires an existing DictionaryManager with:
 *   - DictionaryManager(String filename) throws FileNotFoundException
 *   - void addWord(String word)
 *   - void removeWord(String word)
 *   - int getFrequency(String word)
 *   - Iterable<String> getAllWords()
 */
public class GUIDriver extends JFrame {

    private DictionaryBuilder dict;

    private final JTextField wordField = new JTextField();
    private final JButton addButton = new JButton("Add");
    private final JButton removeButton = new JButton("Remove");
    private final JButton searchButton = new JButton("Search");
    private final JButton listButton = new JButton("List All");
    private final JButton quitButton = new JButton("Quit");

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> wordList = new JList<>(listModel);

    private final JTextArea console = new JTextArea(6, 40);
    private final JLabel status = new JLabel("Ready.");

    public GUIDriver(String initialFilename) {
        super("Project 4 - Dictionary Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 520));
        setLocationByPlatform(true);

        // UI Layout
        setJMenuBar(buildMenuBar());
        setContentPane(buildContent());
        wireActions();

        // IMPORTANT: put the renderer INSIDE the constructor (or an init block),
        // not at the top level of the class body.
        wordList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String w = (String) value;
                int c = 0;
                if (dict != null) {
                    try { c = dict.getFrequency(w); } catch (Exception ignored) {}
                }
                lbl.setText(w + " (" + c + ")");
                return lbl;
            }
        });

        // Load dictionary
        maybeLoadDictionary(initialFilename);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open…");
        JMenuItem exit = new JMenuItem("Exit");

        open.addActionListener(e -> chooseAndLoadFile());
        exit.addActionListener(e -> safeExit());

        // Cross-version (Java 8/9+) shortcut mask
        int mask = menuShortcutMask();
        if (mask != 0) {
            open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, mask));
            exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask));
        }

        file.add(open);
        file.addSeparator();
        file.add(exit);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Project 4 Swing UI\nCommands: add, remove/delete, search, list, quit/exit",
                "About", JOptionPane.INFORMATION_MESSAGE
        ));
        help.add(about);

        bar.add(file);
        bar.add(help);
        return bar;
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top: input + action buttons
        JPanel top = new JPanel(new BorderLayout(8, 8));
        wordField.setToolTipText("Enter a word…");
        top.add(wordField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 5, 8, 8));
        buttons.add(addButton);
        buttons.add(removeButton);
        buttons.add(searchButton);
        buttons.add(listButton);
        buttons.add(quitButton);
        top.add(buttons, BorderLayout.EAST);

        // Center split: list on left, console on right
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);

        JPanel left = new JPanel(new BorderLayout(6, 6));
        wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JLabel("Dictionary Words:"), BorderLayout.NORTH);
        left.add(new JScrollPane(wordList), BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(6, 6));
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        right.add(new JLabel("Console Interaction (messages):"), BorderLayout.NORTH);
        right.add(new JScrollPane(console), BorderLayout.CENTER);

        split.setLeftComponent(left);
        split.setRightComponent(right);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(new EmptyBorder(6, 0, 0, 0));
        status.setBorder(new EmptyBorder(2, 4, 2, 4));
        statusBar.add(status, BorderLayout.WEST);

        root.add(top, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);
        return root;
    }

    private void wireActions() {
        wordField.addActionListener(e -> doSearch());
        addButton.addActionListener(e -> doAdd());
        removeButton.addActionListener(e -> doRemove());
        searchButton.addActionListener(e -> doSearch());
        listButton.addActionListener(e -> refreshList());
        quitButton.addActionListener(e -> safeExit());

        // Double-click list item to copy into the text field
        wordList.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String sel = wordList.getSelectedValue();
                    if (sel != null) wordField.setText(sel);
                }
            }
        });

        // Right-click popup on list
        JPopupMenu popup = new JPopupMenu();
        JMenuItem searchItem = new JMenuItem("Search this word");
        JMenuItem removeItem = new JMenuItem("Remove this word");
        searchItem.addActionListener(e -> {
            String sel = wordList.getSelectedValue();
            if (sel != null) { wordField.setText(sel); doSearch(); }
        });
        removeItem.addActionListener(e -> {
            String sel = wordList.getSelectedValue();
            if (sel != null) { wordField.setText(sel); doRemove(); }
        });
        popup.add(searchItem);
        popup.add(removeItem);

        wordList.setComponentPopupMenu(popup);
        wordList.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
            @Override public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int idx = wordList.locationToIndex(e.getPoint());
                    if (idx >= 0) {
                        wordList.setSelectedIndex(idx);
                        popup.show(wordList, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void maybeLoadDictionary(String filenameFromArgs) {
        String filename = filenameFromArgs;
        if (filename == null || filename.isBlank()) { chooseAndLoadFile(); return; }
        tryInitDictionary(filename);
    }

    private void chooseAndLoadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select dictionary input file…");
        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            tryInitDictionary(f.getAbsolutePath());
        } else {
            appendConsole("No file selected.");
            setStatus("Ready (no file loaded)");
        }
    }

    private void tryInitDictionary(String filename) {
        try {
            dict = new DictionaryBuilder(filename);
            appendConsole("Loaded dictionary from: " + filename);
            setStatus("Loaded: " + filename);
            refreshList();
        } catch (FileNotFoundException e) {
            dict = null;
            appendConsole("Unable to open input file: " + filename);
            setStatus("Error loading: " + filename);
            JOptionPane.showMessageDialog(this, "Unable to open input file:\n" + filename,
                    "File Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            dict = null;
            appendConsole("Unable to initialize DictionaryManager (" + e.getMessage() + ")");
            setStatus("Initialization failed");
            JOptionPane.showMessageDialog(this, "Unable to initialize DictionaryManager.\n" + e,
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doAdd() {
        if (!ensureDict()) return;
        String w = normalizedWord();
        if (w == null) return;
        dict.addWord(w);
        appendConsole("> add " + w);
        setStatus("Added: " + w);
        refreshListPreserveSelection(w);
        wordField.selectAll();
        wordField.requestFocusInWindow();
    }

    private void doRemove() {
        if (!ensureDict()) return;
        String w = normalizedWord();
        if (w == null) return;
        try {
            dict.removeWord(w);
        } catch (DictionaryEntryNotFoundException e) {}
        appendConsole("> remove " + w);
        setStatus("Removed (or attempted): " + w);
        refreshList();
        wordField.selectAll();
        wordField.requestFocusInWindow();
    }

    private void doSearch() {
        if (!ensureDict()) return;
        String w = normalizedWord();
        if (w == null) return;
        int count = dict.getFrequency(w);
        String msg = (count == 0)
                ? ("\"" + w + "\" not found.")
                : (count + " instance(s) of \"" + w + "\" found.");
        appendConsole(msg);
        setStatus(msg);
        if (count >= 0) JOptionPane.showMessageDialog(this, msg, "Search", JOptionPane.INFORMATION_MESSAGE);
        selectInList(w);
    }

    private void refreshList() {
        if (!ensureDict()) return;
        listModel.clear();
        for (String s : dict.getAllWords()) {
            listModel.addElement(s);
        }
        setStatus("List refreshed (" + listModel.size() + " items)");
        wordList.repaint();
    }

    private void refreshListPreserveSelection(String preferred) {
        String current = wordList.getSelectedValue();
        refreshList();
        if (preferred != null) selectInList(preferred);
        else if (current != null) selectInList(current);
    }

    private void selectInList(String value) {
        if (value == null) return;
        for (int i = 0; i < listModel.size(); i++) {
            if (value.equals(listModel.get(i))) {
                wordList.setSelectedIndex(i);
                wordList.ensureIndexIsVisible(i);
                break;
            }
        }
    }

    private boolean ensureDict() {
        if (dict != null) return true;
        JOptionPane.showMessageDialog(this,
                "No dictionary file loaded. Please open a file (File → Open…).",
                "No Dictionary Loaded", JOptionPane.WARNING_MESSAGE);
        setStatus("Please load a dictionary file.");
        return false;
    }

    private String normalizedWord() {
        String w = wordField.getText();
        if (w == null) w = "";
        w = w.trim();
        if (w.isEmpty()) {
            setStatus("Enter a word first.");
            appendConsole("Unrecognized command: (empty word)");
            return null;
        }
        return w;
    }

    private void appendConsole(String s) {
        console.append(s + System.lineSeparator());
        console.setCaretPosition(console.getDocument().getLength());
    }

    private void setStatus(String s) { status.setText(s); }

    private void safeExit() {
        appendConsole("Quitting…");
        dispose();
        System.exit(0);
    }

    // Cross-version helper for ⌘/Ctrl modifiers
    private static int menuShortcutMask() {
        try {
            // Java 9+
            return (int) Toolkit.class.getMethod("getMenuShortcutKeyMaskEx").invoke(Toolkit.getDefaultToolkit());
        } catch (ReflectiveOperationException ex) {
            try {
                // Java 8
                return (int) Toolkit.class.getMethod("getMenuShortcutKeyMask").invoke(Toolkit.getDefaultToolkit());
            } catch (ReflectiveOperationException ignored) {
                return 0;
            }
        }
    }

    // -------------------- Entry Point --------------------
    public static void main(String[] args) {
        String filenameFromArgs = null;
        if (args != null && args.length == 2) {
            filenameFromArgs = args[1];
        } else if (args != null && args.length >= 2) {
            for (int i = 0; i < args.length - 1; i++) {
                if ("--file".equals(args[i]) || "-f".equals(args[i])) {
                    filenameFromArgs = args[i + 1];
                    break;
                }
            }
        }
        final String initialFilename = filenameFromArgs;
        SwingUtilities.invokeLater(() -> {
            GUIDriver app = new GUIDriver(initialFilename);
            app.setVisible(true);
        });
    }
}
