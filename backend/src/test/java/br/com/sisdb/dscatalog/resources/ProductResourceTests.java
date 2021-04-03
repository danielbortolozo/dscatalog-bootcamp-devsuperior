package br.com.sisdb.dscatalog.resources;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import br.com.sisdb.dscatalog.dto.ProductDTO;
import br.com.sisdb.dscatalog.entities.ProductFactory;
import br.com.sisdb.dscatalog.services.ProductService;
import br.com.sisdb.dscatalog.services.exceptions.DataBaseException;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;


    private Long existingId;
    private long nonExistingId;
    private Long dependentId;
    private ProductDTO newProductDTO;
    private ProductDTO existingProductDTO;
    private PageImpl<ProductDTO> page;
    private String operatorUsername;
    private String operatorPassword;

    @BeforeEach
    void setUp() throws Exception {
       existingId = 1L;
       nonExistingId = 2L;
       dependentId = 3L;
       newProductDTO = ProductFactory.createProductDTO(null);
       existingProductDTO = ProductFactory.createProductDTO(existingId);
       operatorUsername = "alex@gmail.com";
       operatorPassword = "123456";

       page = new PageImpl<>(List.of(existingProductDTO));
       Mockito.when(service.findById(existingId)).thenReturn(existingProductDTO);
       Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
       Mockito.when(service.findAllPaged(any(), anyString(), any())).thenReturn(page);

       Mockito.when(service.inster(any())).thenReturn(existingProductDTO);

       Mockito.when(service.update(eq(existingId), any())).thenReturn(existingProductDTO);
       Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

       doNothing().when(service).delete(existingId);
       doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
       doThrow(DataBaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
        ResultActions resultActions =
                mockMvc.perform(delete("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer "+accessToken)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);

        ResultActions resultActions =
                mockMvc.perform(delete("/products/{id}", existingId)
                        .header("Authorization", "Bearer "+accessToken)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNoContent());
    }


    @Test
    public void insertShouldReturnProductDTOWhenCreateNewProduct() throws Exception {
        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
        String jsonBody = objectMapper.writeValueAsString(newProductDTO);

        ResultActions resultActions =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer "+accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void insertShouldReturnUnprocessableWhenCreateNewProduct() throws Exception {
        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
        ProductDTO productDTO = newProductDTO;
        productDTO.setPrice(0.0);
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions =
                mockMvc.perform(post("/products")
                        .header("Authorization", "Bearer "+accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isUnprocessableEntity());
    }


    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
        String jsonBody = objectMapper.writeValueAsString(newProductDTO);
        String expectedName = newProductDTO.getName();
        Double expectedPrice = newProductDTO.getPrice();
        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", existingId)
                        .header("Authorization", "Bearer "+accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
        resultActions.andExpect(jsonPath("$.name").value(expectedName));
        resultActions.andExpect(jsonPath("$.price").value(expectedPrice));

    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String accessToken = obtainAccessToken(operatorUsername, operatorPassword);
        String jsonBody = objectMapper.writeValueAsString(newProductDTO);
        ResultActions resultActions =
                mockMvc.perform(put("/products/{id}", nonExistingId)
                        .header("Authorization", "Bearer "+accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(get("/products", existingId)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.content").exists());

    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions resultActions =
        mockMvc.perform(get("/products/{id}", existingId)
              .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.id").value(existingId));


    }
    @Test
    public void findByIdShoundReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions resultActions =
                mockMvc.perform(get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", clientId);
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, clientSecret))
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
