package tripService.service;

import com.travel.user.grpc.UserGrpcServiceGrpc;
import com.travel.user.grpc.ValidateUserRequest;
import com.travel.user.grpc.ValidateUserResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userStub;

    public boolean validateUser(String userId) {
        try {
            ValidateUserRequest request = ValidateUserRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            ValidateUserResponse response = userStub.validateUser(request);
            return response.getValid();
        } catch (StatusRuntimeException e) {
            log.error("gRPC call to user-service failed for userId={}: {}", userId, e.getStatus());
            return false;
        }
    }
}
