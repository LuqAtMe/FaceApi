package home.application.facedetect;

import home.application.facedetect.model.FaceObject;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class FaceApiClient {

    private static final String FACE_BASE_API_URL = "https://westeurope.api.cognitive.microsoft.com/face/v1.0/detect?";
    private static final String IMAGE_URL = "https://www.declinic.pl/wp-content/uploads/2019/02/DeClinic_Mirella_Ziomek-1.jpg";
    @Value("${Ocp-Apim-Subscription-Key}")
    private String ocpApimSubscriptionKey;


    public FaceObject[] getInfoByPhotoUrl(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ImageUrl> imageUrlHttpEntity = getEntity(url);
        ResponseEntity<FaceObject[]> exchange = restTemplate.exchange(getApiUrl(),
                HttpMethod.POST,
                imageUrlHttpEntity,
                FaceObject[].class);
        return exchange.getBody();

    }

    private HttpEntity<ImageUrl> getEntity(String ImageURL) {
        ImageUrl imageUrl = new ImageUrl(ImageURL);
        HttpHeaders httpHeaders = getHttpHeaders();
        return new HttpEntity<>(imageUrl, httpHeaders);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key", ocpApimSubscriptionKey);
        return httpHeaders;
    }

    private URI getApiUrl() {

        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(FACE_BASE_API_URL);
            uriBuilder.addParameter("returnFaceId", "true");
            uriBuilder.addParameter("returnFaceAttributes", "age,gender,smile,facialHair,glasses,emotion,hair,makeup,occlusion");
            uriBuilder.addParameter("returnFaceLandmarks", "false");
            uriBuilder.addParameter("recognitionModel", "recognition_01");
            uriBuilder.addParameter("returnRecognitionModel", "false");
            uriBuilder.addParameter("detectionModel", "detection_01");
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }


}
