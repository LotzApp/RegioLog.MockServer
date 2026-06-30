package org.lotzapp.component;

import org.lotzapp.regiologapi.model.Order;
import org.lotzapp.regiologapi.model.OrderInsert;
import org.lotzapp.regiologapi.model.OrderPosition;
import org.lotzapp.regiologapi.model.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderComponent {

    private final List<Order> ORDER_LIST = new ArrayList<>();

    private final ProductComponent productComponent;
    private final ClientComponent clientComponent;

    @Autowired
    public OrderComponent(ProductComponent productComponent, ClientComponent clientComponent) {
        this.productComponent = productComponent;
        this.clientComponent = clientComponent;
    }

    public void initializeData() {
        var order = getOrder(OrderType.ORDER, 10.99);
        ORDER_LIST.add(order);
        ORDER_LIST.add(getCorrectionOrder(order));
        ORDER_LIST.add(getOrder(OrderType.ORDER, 12.99));
        ORDER_LIST.add(getOrder(OrderType.CONFIRMATION, 13.99));
        ORDER_LIST.add(getOrder(OrderType.CORRECTION, 18.99));
    }

    public ResponseEntity<List<Order>> getOrders(Object[] args) {
        return ResponseEntity.ok(ORDER_LIST);
    }

    public List<Order> getOrder(Object[] args) {
        var id = (UUID) args[0];
        return ORDER_LIST.stream()
                .filter(o -> o.getId().equals(id))
                .toList();
    }

    /*
        Type = Confirmation / COrrection
        via OrderId link to parent Order

     */

    private Order getCorrectionOrder(Order parentOrder) {
        var order = new Order(
                OffsetDateTime.now().minusDays(10),
                OffsetDateTime.now().minusDays(5),
                parentOrder.getId(),
                clientComponent.getClientList().getFirst().getId().get(),
                1,
                OffsetDateTime.now()
        );
        order.type(OrderType.CORRECTION);
        order.orderId(parentOrder.getId());
        var positions = new ArrayList<OrderPosition>();
        for(var position : parentOrder.getPositions().get()) {
            positions.add(copyPosition(position));
        }
        order.positions(positions);
        return order;
    }

    private OrderPosition copyPosition(OrderPosition template) {
        var position = new OrderPosition(
                template.getProductName(),
                new BigDecimal("5"),
                template.getUnitId(),
                template.getId(),
                OffsetDateTime.now()
        );
        position.productId(template.getProductId().get());
        position.vat(template.getVat().get());
        position.valueNet(template.getValueNet().get());
        position.valueGross(template.getValueGross().get());
        return position;
    }

    private Order getOrder(OrderType type, double net) {
        var order = new Order(
                OffsetDateTime.now().minusDays(10),
                OffsetDateTime.now().minusDays(5),
                UUID.randomUUID(),
                clientComponent.getClientList().getFirst().getId().get(),
                1,
                OffsetDateTime.now()
        );
        order.information("This is an order information.");
        order.net(new BigDecimal(String.valueOf(net)));
        order.gross(new BigDecimal(String.valueOf(net * 1.20)));
        order.type(type);
        order.positions(List.of(getOrderPosition()));
        order.number(UUID.randomUUID().toString());
        order.subject("Test Order");
        return order;
    }

    private OrderPosition getOrderPosition() {
        var position = new OrderPosition(
                "TestProduct mit langem Namen",
                BigDecimal.valueOf(100),
                1,
                UUID.randomUUID(),
                OffsetDateTime.now()
        );
        position.productId(productComponent.getProducts().getFirst().getId());
        position.vat(BigDecimal.valueOf(20));
        position.valueNet(BigDecimal.valueOf(100));
        position.valueGross(BigDecimal.valueOf(120));
        return position;
    }

    public ResponseEntity<Order> addOrder(Object[] args) {
        var orderToInsert = (OrderInsert) args[0];
        var orderTemplate = getOrder(OrderType.ORDER, 10.0d);
        return ResponseEntity.ok(orderTemplate);
    }
}
