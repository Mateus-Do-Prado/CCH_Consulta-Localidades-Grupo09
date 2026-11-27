package utfpr;

import javax.swing.SwingUtilities;
import utfpr.http.ClienteHttp;
import utfpr.servico.LocalServico;
import utfpr.view.MainFrame;

public class Main {

    public static void main(String[] args) {
        LocalServico servico = new LocalServico(new ClienteHttp());
        SwingUtilities.invokeLater(() -> new MainFrame(servico).setVisible(true));
    }
}