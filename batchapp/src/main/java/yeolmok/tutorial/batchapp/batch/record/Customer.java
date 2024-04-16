package yeolmok.tutorial.batchapp.batch.record;


/*
 * Java Record
 * 1. final class, private final fields
 * 2. Getter method
 * 3. equals()
 * 4. hashCode()
 * 5. toString()
 */
public record Customer(int id, String name, int point, int couponCount) {}
