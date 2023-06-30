import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TemplateManagerWindow extends JFrame {
    private JList<Template> templateList;
    private DefaultListModel<Template> listModel;

    public TemplateManagerWindow(List<Template> templates) {
        setTitle("Template Manager");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        for (Template template : templates) {
            listModel.addElement(template);
        }

        templateList = new JList<>(listModel);
        templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        templateList.setCellRenderer(new TemplateCellRenderer());

        JScrollPane scrollPane = new JScrollPane(templateList);
        add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Löschen");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = templateList.getSelectedIndex();
                if (selectedIndex != -1) {
                    listModel.remove(selectedIndex);
                }
            }
        });

        JButton renameButton = new JButton("Umbenennen");
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = templateList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Template selectedTemplate = listModel.getElementAt(selectedIndex);
                    String newName = JOptionPane.showInputDialog("Geben Sie den neuen Namen ein:");
                    if (newName != null && !newName.isEmpty()) {
                        selectedTemplate.setName(newName);
                        listModel.setElementAt(selectedTemplate, selectedIndex);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(renameButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private static class TemplateCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Template) {
                Template template = (Template) value;
                value = template.getName();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
