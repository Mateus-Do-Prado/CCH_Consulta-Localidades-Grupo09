package utfpr.servico;

public class LocalidadesException extends RuntimeException {

    public LocalidadesException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}