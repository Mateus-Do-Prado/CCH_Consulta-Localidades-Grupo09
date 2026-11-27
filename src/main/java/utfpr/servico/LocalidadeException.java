package utfpr.servico;

public class LocalidadeException extends RuntimeException {

    public LocalidadeException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}