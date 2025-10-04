package ninja.rail.utils;

public final class DateUtils {
    private DateUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static int[] parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("Дата не может быть пустой или null");
        }
        String[] parts = dateStr.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Дата должна быть в формате dd.MM.yyyy");
        }
        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return new int[]{day, month, year};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Дата содержит недопустимые значения", e);
        }
    }

}
