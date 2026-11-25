package utfpr.view;
import utfpr.model.Municipio;

import javax.swing.*;
import java.awt.*;

public class DetalheMunicipioDialog extends JDialog {
    public DetalheMunicipioDialog(JFrame pai, Municipio m) {
        super(pai, "Detalhes do município", true);

        JPanel painel = new JPanel(new GridLayout(0, 1, 0, 6));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        painel.add(new JLabel("ID: " + m.getId()));
        painel.add(new JLabel("Nome: " + m.getNome()));
        painel.add(new JLabel("UF: " + m.getSiglaUf()));
        painel.add(new JLabel("Região: " + m.getRegiao()));

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rodape.add(btnFechar);

        setLayout(new BorderLayout());
        add(painel, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setMinimumSize(new Dimension(300, getHeight()));
        setLocationRelativeTo(pai);
    }
    
}
