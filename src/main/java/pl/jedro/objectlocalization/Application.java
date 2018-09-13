package pl.jedro.objectlocalization;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.vision.v1p3beta1.ImageAnnotatorClient;
import com.google.cloud.vision.v1p3beta1.ImageAnnotatorSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@SpringBootApplication
@Configuration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ImageAnnotatorClient imageAnnotatorClient(CredentialsProvider credentialsProvider) throws IOException {
		ImageAnnotatorSettings clientSettings = ImageAnnotatorSettings.newBuilder()
				.setCredentialsProvider(credentialsProvider)
				.build();

		return ImageAnnotatorClient.create(clientSettings);
	}
}
