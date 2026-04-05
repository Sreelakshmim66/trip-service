package tripService.service;

import com.internalproject.user_service.grpc.UserServiceGrpc;
import com.internalproject.user_service.grpc.ValidateUserRequest;
import com.internalproject.user_service.grpc.ValidateUserResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserGrpcClient {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

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
