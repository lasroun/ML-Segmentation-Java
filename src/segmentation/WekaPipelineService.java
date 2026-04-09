package segmentation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

public class WekaPipelineService {

    public Instances loadCsv(File file) throws Exception {
        Instances data = parseCsvFallback(file);

        if (data == null || data.numInstances() == 0 || data.numAttributes() == 0) {
            throw new IllegalStateException("Le CSV charge est vide ou invalide.");
        }

        return data;
    }

    private Instances parseCsvFallback(File file) throws Exception {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split(",", -1));
                }
            }
        }

        if (rows.isEmpty()) {
            throw new IllegalStateException("CSV vide.");
        }

        String[] headers = rows.get(0);
        int columnCount = headers.length;
        List<Boolean> numericFlags = detectNumericColumns(rows, columnCount);
        ArrayList<Attribute> attributes = new ArrayList<>();

        for (int i = 0; i < columnCount; i++) {
            String attrName = headers[i].trim();
            if (numericFlags.get(i)) {
                attributes.add(new Attribute(attrName));
            } else {
                attributes.add(new Attribute(attrName, (List<String>) null));
            }
        }

        Instances data = new Instances("csv_data", attributes, rows.size() - 1);

        for (int r = 1; r < rows.size(); r++) {
            String[] cols = rows.get(r);
            DenseInstance instance = new DenseInstance(columnCount);
            instance.setDataset(data);

            for (int c = 0; c < columnCount; c++) {
                String value = c < cols.length ? cols[c].trim() : "";
                if (value.isEmpty()) {
                    instance.setMissing(c);
                } else if (numericFlags.get(c)) {
                    try {
                        instance.setValue(c, Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        instance.setMissing(c);
                    }
                } else {
                    instance.setValue(c, value);
                }
            }

            data.add(instance);
        }

        return data;
    }

    private List<Boolean> detectNumericColumns(List<String[]> rows, int columnCount) {
        List<Boolean> numericFlags = new ArrayList<>();

        for (int c = 0; c < columnCount; c++) {
            boolean numeric = true;
            for (int r = 1; r < rows.size(); r++) {
                String[] cols = rows.get(r);
                if (c >= cols.length) {
                    continue;
                }
                String value = cols[c].trim();
                if (value.isEmpty()) {
                    continue;
                }
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    numeric = false;
                    break;
                }
            }
            numericFlags.add(numeric);
        }

        return numericFlags;
    }

    public Instances removeTextColumns(Instances data) {
        Instances filtered = new Instances(data);
        List<String> removedAttributes = new ArrayList<>();

        for (int i = filtered.numAttributes() - 1; i >= 0; i--) {
            Attribute attribute = filtered.attribute(i);
            if (!attribute.isNumeric()) {
                removedAttributes.add(attribute.name());
                filtered.deleteAttributeAt(i);
            }
        }

        if (!removedAttributes.isEmpty()) {
            System.out.println("Colonnes supprimees (non numeriques): " + removedAttributes);
        }

        if (filtered.numAttributes() == 0) {
            throw new IllegalStateException("Aucune colonne numerique restante apres filtrage.");
        }

        return filtered;
    }

    public Instances normalize(Instances data) throws Exception {
        Normalize normalize = new Normalize();
        normalize.setInputFormat(data);
        return Filter.useFilter(data, normalize);
    }

    public Instances prepareData(File file) throws Exception {
        Instances loaded = loadCsv(file);
        logDataset("Apres chargement", loaded);

        Instances numericOnly = removeTextColumns(loaded);
        logDataset("Apres suppression colonnes texte", numericOnly);

        Instances normalized = normalize(numericOnly);
        logDataset("Apres normalisation", normalized);

        return normalized;
    }

    public void logDataset(String step, Instances data) {
        System.out.println("=== " + step + " ===");
        System.out.println("Instances: " + data.numInstances());
        System.out.println("Attributs: " + data.numAttributes());

        StringBuilder builder = new StringBuilder("Attributs restants: ");
        for (int i = 0; i < data.numAttributes(); i++) {
            builder.append(data.attribute(i).name());
            if (i < data.numAttributes() - 1) {
                builder.append(", ");
            }
        }
        System.out.println(builder);
    }
}
