public class Product {
    private String id;
    private String gun;        // contoh: AK-47, AWP, USP-S
    private String skinName;   // contoh: Redline, Asiimov, Blaze
    private double skinPrice;
    private String skinWear;   // contoh: Factory New, Minimal Wear, dll.

    // CONSTRUCTOR
    public Product(String id, String gun, String skinName, double skinPrice, String skinWear) {
        this.id = id;
        this.gun = gun;
        this.skinName = skinName;
        this.skinPrice = skinPrice;
        this.skinWear = skinWear;
    }

    // GETTERS
    public String getId() { return id; }
    public String getGun() { return gun; }
    public String getSkinName() { return skinName; }
    public double getSkinPrice() { return skinPrice; }
    public String getSkinWear() { return skinWear; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setGun(String gun) { this.gun = gun; }
    public void setSkinName(String skinName) { this.skinName = skinName; }
    public void setSkinPrice(double skinPrice) { this.skinPrice = skinPrice; }
    public void setSkinWear(String skinWear) { this.skinWear = skinWear; }
}
