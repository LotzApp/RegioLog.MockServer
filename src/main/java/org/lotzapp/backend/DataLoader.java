package org.lotzapp.backend;

import lombok.extern.slf4j.Slf4j;
import org.lotzapp.component.ClientComponent;
import org.lotzapp.component.OrderComponent;
import org.lotzapp.component.ProductComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {
    private final ClientComponent clientComponent;
    private final ProductComponent productComponent;
    private final OrderComponent orderComponent;

    @Autowired
    public DataLoader(ClientComponent clientComponent, ProductComponent productComponent, OrderComponent orderComponent) {
        this.clientComponent = clientComponent;
        this.productComponent = productComponent;
        this.orderComponent = orderComponent;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading data for clients");
        clientComponent.initializeData();
        log.info("Loading data for products");
        productComponent.initializeData();
        log.info("Loading data for orders");
        orderComponent.initializeData();

    }
}
