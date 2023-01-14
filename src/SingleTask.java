public class SingleTask extends Task {
    private final Status status;

    public SingleTask(int id, String name, Status status) {
        super(id, name);
        this.status = status;
    }

    public SingleTask updateStatus(Status status) {
        return new SingleTask(this.getId(), this.getName(), status);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Type getType() {
        return Type.SINGLE;
    }

    @Override
    public String toString() {
        return "SingleTask{" +
                "id = " + getId() +
                ", name = '" + getName() + '\'' +
                ", status = '" + getStatus() + '\'' +
                "}";
    }

    public static class Creator {
        private String name;

        public Creator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
