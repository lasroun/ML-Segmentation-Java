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

/** Charge un CSV en {@link Instances} Weka puis filtre et normalise pour le clustering. */
public class WekaPipelineService {

    /** Charge le fichier via le parseur interne et vérifie que le jeu n'est pas vide. */
    public Instances loadCsv(File file) throws Exception {
        Instances data = parseCsvFallback(file);

        if (data == null || data.numInstances() == 0 || data.numAttributes() == 0) {
            throw new IllegalStateException("Le CSV charge est vide ou invalide.");
        }

        return data;
    }

    /** Parse ligne par ligne (virgule), infère types numériques vs texte et construit le header Weka. */
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

    /** Pour chaque colonne, indique si toutes les valeurs d'exemple sont parseables en nombre. */
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

    /** Supprime les attributs non numériques pour ne garder que des colonnes exploitables par les clusterers. */
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

    /** Applique le filtre Weka {@link Normalize} sur une copie des données. */
    public Instances normalize(Instances data) throws Exception {
        Normalize normalize = new Normalize();
        normalize.setInputFormat(data);
        return Filter.useFilter(data, normalize);
    }

    /** Chaîne complète : chargement, suppression colonnes texte, normalisation, avec journaux console. */
    public Instances prepareData(File file) throws Exception {
        Instances loaded = loadCsv(file);
        logDataset("Apres chargement", loaded);

        Instances numericOnly = removeTextColumns(loaded);
        logDataset("Apres suppression colonnes texte", numericOnly);

        Instances normalized = normalize(numericOnly);
        logDataset("Apres normalisation", normalized);

        return normalized;
    }

    /** Affiche sur la console le nombre d'instances, d'attributs et leurs noms pour une étape donnée. */
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
