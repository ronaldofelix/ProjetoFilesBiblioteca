package projeto.multimidia.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3Util {
        private static final String BUCKET = "AQUI-O-NOME-DO-SEU-BUCKET";

        public static void uploadArquivo(String fileName, InputStream inputStream)
                        throws S3Exception, AwsServiceException, SdkClientException, IOException {

                AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AQUI-SUA-CHAVE-DE-ACESSO",
                                "AQUI-SUA-SECRET-KEY");
                StaticCredentialsProvider credsProvider = StaticCredentialsProvider.create(awsCreds);

                S3Client client = S3Client.builder()
                                .region(Region.SA_EAST_1) // REGIAO DO SEU BUCKET
                                .credentialsProvider(credsProvider)
                                .build();

                PutObjectRequest request = PutObjectRequest.builder()
                                .bucket(BUCKET)
                                .key(fileName)
                                .build();

                client.putObject(request, RequestBody.fromInputStream(inputStream, inputStream.available()));
        }

        public static InputStreamResource downloadArquivo(String fileName) {
                AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AQUI-SUA-CHAVE-DE-ACESSO",
                                "AQUI-SUA-SECRET-KEY");
                StaticCredentialsProvider credsProvider = StaticCredentialsProvider.create(awsCreds);

                S3Client client = S3Client.builder()
                                .region(Region.SA_EAST_1) // REGIAO DO SEU BUCKET
                                .credentialsProvider(credsProvider)
                                .build();

                GetObjectRequest request = GetObjectRequest.builder()
                                .bucket(BUCKET)
                                .key(fileName)
                                .build();

                ResponseInputStream<GetObjectResponse> response = client.getObject(request);
                InputStreamResource resource = new InputStreamResource(response);

                return resource;
        }

        public static List<String> listarArquivos() {

                AwsBasicCredentials awsCreds = AwsBasicCredentials.create("AQUI-SUA-CHAVE-DE-ACESSO",
                                "AQUI-SUA-SECRET-KEY");
                StaticCredentialsProvider credsProvider = StaticCredentialsProvider.create(awsCreds);

                S3Client client = S3Client.builder()
                                .region(Region.SA_EAST_1) // REGIAO DO SEU BUCKET
                                .credentialsProvider(credsProvider)
                                .build();

                ListObjectsV2Request request = ListObjectsV2Request.builder()
                                .bucket(BUCKET)
                                .build();

                ListObjectsV2Response response = client.listObjectsV2(request);

                List<S3Object> objects = response.contents();
                List<String> arquivos = new ArrayList<>(); // PEGANDO TODOS OS ARQUIVOS E PONDO EM UMA ARRAYLIST
                for (S3Object object : objects) {
                        arquivos.add(object.key());
                }

                return arquivos;
        }

}