package application.orders.enums;

public enum OrderStatus {
    WAITING_APPROVAL() {
        @Override
        public String getTranslated() {
            return "Aguardando aprovação";
        }
    },
    CANCELED() {
        @Override
        public String getTranslated() {
            return "Cancelado";
        }
    },
    APPROVED() {
        @Override
        public String getTranslated() {
            return "Aprovado";
        }
    };

    public abstract String getTranslated();
}
