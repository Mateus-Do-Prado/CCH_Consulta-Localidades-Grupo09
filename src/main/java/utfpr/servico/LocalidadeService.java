package utfpr.servico;

import java.util.List;
import utfpr.model.Municipio;
import utfpr.model.UF;

public interface LocalidadeService {

    List<UF> listarUFs();

    List<Municipio> listarMunicipios(String siglaUf);
}
