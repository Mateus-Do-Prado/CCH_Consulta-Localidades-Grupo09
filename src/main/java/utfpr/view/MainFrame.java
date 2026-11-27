package utfpr.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import utfpr.model.Municipio;
import utfpr.model.UF;
import utfpr.servico.LocalidadeService;

public class MainFrame extends JFrame {

    private final LocalidadeService service;
    private final JComboBox<UF> cbUf = new JComboBox<>();
    private final JButton btCarregar = new JButton("Carregar");
    private final DefaultListModel<Municipio> modelo = new DefaultListModel<>();
    private final JList<Municipio> listaMunicipios = new JList<>(modelo);

    public MainFrame(LocalidadeService service) {
        super("UF e Municípios");
        this.service = service;

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topo.add(new JLabel("UF:"));
        topo.add(cbUf);
        topo.add(btCarregar);

        listaMunicipios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel centro = new JPanel(new BorderLayout(0, 4));
        centro.setBorder(new EmptyBorder(0, 12, 12, 12));
        centro.add(new JLabel("Municípios:"), BorderLayout.NORTH);
        centro.add(new JScrollPane(listaMunicipios), BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        btCarregar.addActionListener(e -> aoClicarCarregar());
        listaMunicipios.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            Municipio m = listaMunicipios.getSelectedValue();
            if (m == null) {
                return;
            }
            new DetalheMunicipioDialog(this, m).setVisible(true);
        });

        setSize(440, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        carregarUFs();
    }

    private void carregarUFs() {
        btCarregar.setEnabled(false);
        new SwingWorker<List<UF>, Void>() {
            @Override
            protected List<UF> doInBackground() {
                return service.listarUFs();
            }

            @Override
            protected void done() {
                try {
                    cbUf.setModel(new DefaultComboBoxModel<>(get().toArray(new UF[0])));
                } catch (Exception ex) {
                    mostrarErro(ex);
                } finally {
                    btCarregar.setEnabled(true);
                }
            }
        }.execute();
    }

    private void aoClicarCarregar() {
        UF uf = (UF) cbUf.getSelectedItem();
        if (uf == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma UF.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        btCarregar.setEnabled(false);
        new SwingWorker<List<Municipio>, Void>() {
            @Override
            protected List<Municipio> doInBackground() {
                return service.listarMunicipios(uf.getSigla());
            }

            @Override
            protected void done() {
                try {
                    List<Municipio> lista = get();
                    modelo.clear();
                    modelo.addAll(lista);
                    if (lista.isEmpty()) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Nenhum município encontrado.");
                    }
                } catch (Exception ex) {
                    mostrarErro(ex);
                } finally {
                    btCarregar.setEnabled(true);
                }
            }
        }.execute();
    }

    private void mostrarErro(Exception ex) {
        Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
        JOptionPane.showMessageDialog(this, causa.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}