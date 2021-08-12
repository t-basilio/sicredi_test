import br.com.sicredi.simulacao.Application;
import br.com.sicredi.simulacao.LoadDatabase;
import br.com.sicredi.simulacao.repository.SimulacaoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.ResourceUtils;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimulacaoTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LoadDatabase loadDatabase;

    @Autowired
    private SimulacaoRepository simulacaoRepository;

    private String jsonSimulacaoSucesso;
    private String jsonSimulacaoFalhaFaltaNome;
    private String getJsonSimulacaoNomeAtualizadoSucesso;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1/simulacoes/";

        jsonSimulacaoSucesso = ResourceUtils.getContentFromResource(
                "/simulacao-sucesso.json");
        jsonSimulacaoFalhaFaltaNome = ResourceUtils.getContentFromResource(
                "/simulacao-falha-falta-nome.json");
        getJsonSimulacaoNomeAtualizadoSucesso = ResourceUtils.getContentFromResource(
                "/simulacao-nome-atualizado-sucesso.json");

        try {
            simulacaoRepository.deleteAll();
            loadDatabase.initDatabaseSimulacao(simulacaoRepository).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deveRetornarStatus200_QuandoListarAsSimulacoes(){
        given()
                .accept(ContentType.JSON)
        .when()
                .get()
        .then()
                .statusCode(HttpStatus.OK.value());
                .body("mensagem", Matchers.equalTo("Simulações encontradas"));
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarSimulacaoEncontrada(){
        given()
                .accept(ContentType.JSON)
        .when()
                .get("17822386034")
        .then()
                .statusCode(HttpStatus.OK.value());
                .body("mensagem", Matchers.equalTo("Simulação encontrada"));
    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarSimulacaoNaoEncontrada(){
        given()
                .accept(ContentType.JSON)
                .when()
                .get("85471203003")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
                .body("mensagem", Matchers.equalTo("Simulação não encontrada"));
    }

    @Test
    public void deveRetornarStatus201_QuandoCriarSimulacao(){
        given()
                .body(jsonSimulacaoSucesso)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CREATED.value());]
                .body("mensagem", Matchers.equalTo("Simulação criada com sucesso"));
    }

    @Test
    public void deveRetornarStatus400_QuandoCriarSimulacaoFaltandoNome(){
        given()
                .body(jsonSimulacaoFalhaFaltaNome)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("erros.nome", Matchers.equalTo("Nome não pode ser vazio"));
    }

    @Test
    public void deveRetornarStatus409_QuandoCriarSimulacaoJaExistente(){
        deveRetornarStatus201_QuandoCriarSimulacao();

        given()
                .body(jsonSimulacaoSucesso)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .post()
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("mensagem", Matchers.equalTo("CPF já existente"));
    }

    @Test
    public void deveRetornarStatus200_QuandoAtualizarNomeSimulacao(){
        given()
                .body(getJsonSimulacaoNomeAtualizadoSucesso)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .put("66414919004")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", Matchers.equalTo("Fulano Atualizado"));
    }

    @Test
    public void deveRetornarStatus404_QuandoAtualizarNomeSimulacaoNaoEncontrada(){
        given()
                .body(getJsonSimulacaoNomeAtualizadoSucesso)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .put("58253209037")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("mensagem", Matchers.equalTo("CPF 58253209037 não encontrado"));
    }

    @Test
    public void deveRetornarStatus409_QuandoAtualizarNomeSimulacaoCPF_Existente(){
        deveRetornarStatus200_QuandoAtualizarNomeSimulacao();

        given()
                .body(getJsonSimulacaoNomeAtualizadoSucesso)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
        .when()
                .put("66414919004")
        .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("mensagem", Matchers.equalTo("CPF 58253209037 já existe"));
    }

    @Test
    public void deveRetornarStatus200_QuandoExcluirSimulacaoCPF_Existente(){
        given()
                .accept(ContentType.JSON)
        .when()
                .delete("66414919004")
        .then()
                .statusCode(HttpStatus.OK.value());
                .body("mensagem", Matchers.equalTo("Simulação removida com sucesso"));
    }
}
