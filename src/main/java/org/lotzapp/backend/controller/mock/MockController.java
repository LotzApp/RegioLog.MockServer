package org.lotzapp.backend.controller.mock;


import org.lotzapp.backend.data.MockResponse;
import org.lotzapp.backend.service.mock.MockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mock")
public class MockController {
    private final MockService mockService;

    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    @PostMapping("response")
    public void setMockResponse(@RequestBody MockResponse mockResponse) {
        if(mockResponse.isReset()) {
            this.mockService.reset(mockResponse.getEndpoint());
        } else {
            this.mockService.setResponse(mockResponse.getEndpoint(), mockResponse.getStatus());
        }
    }
}
