package ke.axle.chassis.wrappers;

public class ConstantsWrapper {
    private Object key;
    private String value;

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ConstantsWrapper{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}
