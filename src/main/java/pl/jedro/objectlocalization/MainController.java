package pl.jedro.objectlocalization;

import com.google.cloud.vision.v1p3beta1.*;
import com.google.cloud.vision.v1p3beta1.Image;
import com.google.protobuf.ByteString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MainController {

    @Autowired
    private ImageAnnotatorClient imageAnnotatorClient;

    @PostMapping(value = "/upload", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] upload(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] bytes = file.getBytes();
        Image image = Image.newBuilder().setContent(ByteString.copyFrom(bytes)).build();

        Feature object = Feature.newBuilder()
                .setType(Feature.Type.OBJECT_LOCALIZATION)
                .build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .setImage(image)
                .addFeatures(object)
                .build();
        BatchAnnotateImagesResponse responses = this.imageAnnotatorClient.batchAnnotateImages(Collections.singletonList(request));


        BufferedImage bufferedImage = drawImage(responses, new ByteArrayInputStream(bytes));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);

        return os.toByteArray();
    }

    private BufferedImage drawImage(BatchAnnotateImagesResponse responses, InputStream image) throws IOException {
        List<LocalizedObjectAnnotation> list = responses.getResponses(0).getLocalizedObjectAnnotationsList();

        BufferedImage taggedPicture = ImageIO.read(image);
        Graphics2D g = (Graphics2D) taggedPicture.getGraphics();
        g.setStroke(new BasicStroke(3));
        g.setColor(Color.BLUE);

        for (LocalizedObjectAnnotation annotation : list) {
            List<NormalizedVertex> normalizedVerticesList = annotation.getBoundingPoly().getNormalizedVerticesList();

            List<Node> nodes = normalizedVerticesList.stream()
                    .map(e -> {
                        return new Node(
                                (int) (e.getX() * taggedPicture.getWidth()),
                                (int) (e.getY() * taggedPicture.getHeight())
                        );
                    })
                    .collect(Collectors.toList());

            for (Node node : nodes) {
                for (Node node1 : nodes) {
                    if (node.x == node1.x || node.y == node1.y) {
                        g.drawLine(node.x, node.y, node1.x, node1.y);
                    }
                }
            }
        }

        return taggedPicture;
    }

    @AllArgsConstructor
    public static class Node {
        public int x;
        public int y;
    }
}