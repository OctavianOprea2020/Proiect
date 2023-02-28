package com.fasttrackit.countriesapplication.service.country;

import com.fasttrackit.countriesapplication.exception.ResourceNotFoundException;
import com.fasttrackit.countriesapplication.model.country.City;
import com.fasttrackit.countriesapplication.model.country.Country;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class CountryService {
    public static int nrDrumuri = 0;
    public static int nLungimeDrum = 0;

    private final CountryRepository countryRepository;

    public CountryService(CountryReader countryReader, CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
        System.out.println("Start Reading countries"); // Octavian
        List<Country> listCountry = countryReader.getCountries();
        System.out.println(listCountry); // Octavian
        //countryRepository.saveAll(countryReader.getCountries());
        System.out.println("Inainte de apel SaveAll avem: " + listCountry.size()); // Octavian
        countryRepository.saveAll(listCountry);
        System.out.println("Dupa apel SaveAll avem - listCountry.size : " + listCountry.size()); // Octavian
        System.out.println("Dupa apel SaveAll avem - countryRepository.count: " + countryRepository.count()); // Octavian
        System.out.println("Finished reading countries");
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public List<Country> getByContinent(String continent) {
        System.out.println("Intrat in getByContinent");
        return countryRepository.findByContinentByQuery(continent);
    }

    //get countries that neighbor X but not neighbor Y :-> returns list of Country objects
    public List<Country> getByNeighbour(String includedNeighbour, String excludedNeighbour) {
        return getAllCountries().stream()
                .filter(c -> c.getNeighbours() != null && c.getNeighbours().size() > 0)
                .filter(c -> c.getNeighbours().contains(includedNeighbour) && !c.getNeighbours().contains(excludedNeighbour))
                .toList();
    }

    public Country getById(int id) {
        return countryRepository.findById((long) id)
                .orElseThrow(() -> new ResourceNotFoundException("Country missing", id));
    }

    public Country deleteById(int id) {
        Country countryToBeDeleted = getById(id);
        countryRepository.deleteById((long) id);
        return countryToBeDeleted;
    }

    public Country add(Country country) {
        return countryRepository.save(country);
    }

    public Country update(int id, Country country) {
        Country countryToBeUpdated = getById(id);
        countryToBeUpdated.setArea(country.getArea());
        countryToBeUpdated.setCapital(country.getCapital());
        countryToBeUpdated.setContinent(country.getContinent());
        countryToBeUpdated.setName(country.getName());
        countryToBeUpdated.setNeighbours(country.getNeighbours());
        countryToBeUpdated.setPopulation(country.getPopulation());
        return countryRepository.save(countryToBeUpdated);
    }

    public List<Country> getCountriesFiltered(String continent, Long minPopulation, Long maxPopulation, String searchText) {
        return countryRepository.getByContinentAndMinPopulationAndMaxPopulation(continent, minPopulation, maxPopulation, searchText + "%");
    }

    public Country patch(long id, String capital, long diffPopulation) {
        Country countryToBeUpdated = getById((int) id);
        countryToBeUpdated.setCapital(new City(capital));
        countryToBeUpdated.setPopulation(countryToBeUpdated.getPopulation() + diffPopulation);
        return countryRepository.save(countryToBeUpdated);
    }

    public Country addCityToCountry(int id, City city) {
        Country country = getById(id);
        city.setCountry(country);
        country.getCities().add(city);
        return countryRepository.save(country);
    }

    public Country addNeighbourToCountry(int id, int neighbourId) {
        Country country = getById(id);
        Country neighbour = getById(neighbourId);
        country.getNeighboursCountries().add(neighbour);
        neighbour.getNeighboursCountries().add(country);
        return countryRepository.save(country);
    }

    public String FindRoute(String sStartCode, String sEndCode, boolean bGetExtendedCode) {

        if ((sStartCode.length() == 0) && (sEndCode.length() == 0))
            return "Eroare: Tarile 'Route From' si 'Route To' sunt nule!";
        else if (sStartCode.length() == 0)
            return "Eroare: Tara 'Route From' este nula!";
        else if (sEndCode.length() == 0)
            return "Eroare: Tara 'Route To' este nula!";

        sStartCode = sStartCode.trim();
        sEndCode = sEndCode.trim();

        if ((sStartCode.length() == 0) && (sEndCode.length() == 0))
            return "Eroare: Tarile 'Route From' si 'Route To' sunt nule!";
        else if (sStartCode.length() == 0)
            return "Eroare: Tara 'Route From' este nula!";
        else if (sEndCode.length() == 0)
            return "Eroare: Tara 'Route To' este nula!";

        sStartCode = sStartCode.toUpperCase();
        sEndCode = sEndCode.toUpperCase();

        if (sStartCode.equals(sEndCode))
            return "Error: 'Route From' and 'Route To' are for the samy country!";

        String[] aCoduriTara = {"AD", "AL", "AT", "BA", "BE", "BG", "BY", "CH", "CY", "CZ"
                , "DE", "DK", "EE", "ES", "FI", "FR", "GR", "HR", "HU", "IE"
                , "IS", "IT", "LI", "LT", "LU", "LV", "MC", "MD", "ME", "MK"
                , "MT", "NL", "NO", "PL", "PT", "RO", "RS", "RU", "SE", "SI"
                , "SK", "SM", "TR", "UA", "UK", "VA"};

        int nIndexStartCode = Arrays.asList(aCoduriTara).indexOf(sStartCode);
        int nIndexEndCode = Arrays.asList(aCoduriTara).indexOf(sEndCode);

        if ((nIndexStartCode < 0) && (nIndexEndCode < 0))
            return "Eroare: 'Route From' si 'Route To' nu reprezinta coduri valide de tara!";
        else if (nIndexStartCode < 0)
            return "Eroare: 'Route From' nu reprezinta un cod valid de tara!";
        else if (nIndexEndCode < 0)
            return "Eroare: 'Route To' nu reprezinta un cod valid de tara!";

        boolean bSOUT = false;
        boolean bSOUTFinal = true;

        int nrMaxTari = 46;

        int nrDrumuri1 = 0;
        int nrDrumuri2 = 0;
        int nrDrumuri3 = 0;
        int nrDrumuri4 = 0;
        int nrDrumuri5 = 0;
        int nrDrumuri6 = 0;
        int nrDrumuri7 = 0;
        int nrDrumuri8 = 0;
        int nrDrumuri9 = 0;
        int nrDrumuri10 = 0;

        enum CoduriTari {
            AD, AL, AT, BA, BE, BG, BY, CH, CY, CZ,
            DE, DK, EE, ES, FI, FR, GR, HR, HU, IE,
            IS, IT, LI, LT, LU, LV, MC, MD, ME, MK,
            MT, NL, NO, PL, PT, RO, RS, RU, SE, SI,
            SK, SM, TR, UA, UK, VA
        }
        ;

        //CoduriTari Start = CoduriTari.PT;
        //CoduriTari End   = CoduriTari.RO;

        boolean bLoopToateTarile = false; // Loop pentru una sau mai multe tari

        HashMap<String, String> veciniTara = new HashMap<String, String>();
        veciniTara.put("AD", "ES~FR");
        veciniTara.put("AL", "GR~ME~MK~RS");
        veciniTara.put("AT", "CH~CZ~DE~HU~IT~LI~SK~SI");
        veciniTara.put("BA", "HR~ME~RS");
        veciniTara.put("BE", "DE~FR~LU~NL");
        veciniTara.put("BG", "GR~MK~RO~RS~TR");
        veciniTara.put("BY", "LT~LV~PL~RU~UA");
        veciniTara.put("CH", "AT~DE~FR~IT~LI");
        veciniTara.put("CY", "");
        veciniTara.put("CZ", "AT~DE~PL~SK");
        veciniTara.put("DE", "AT~BE~CH~CZ~DK~FR~LU~NL~PL");
        veciniTara.put("DK", "DE");
        veciniTara.put("EE", "LV~RU");
        veciniTara.put("ES", "AD~FR~PT");
        veciniTara.put("FI", "NO~RU~SE");
        veciniTara.put("FR", "AD~BE~CH~DE~ES~IT~LU~MC");
        veciniTara.put("GR", "AL~BG~MK~TR");
        veciniTara.put("HR", "BA~HU~ME~RS~SI");
        veciniTara.put("HU", "AT~HR~RO~RS~SK~SI~UA");
        veciniTara.put("IE", "UK");
        veciniTara.put("IS", "");
        veciniTara.put("IT", "AT~CH~FR~SI~SM~VA");
        veciniTara.put("LI", "AT~CH");
        veciniTara.put("LT", "BY~LV~PL");
        veciniTara.put("LU", "BE~DE~FR");
        veciniTara.put("LV", "BY~EE~LT~RU");
        veciniTara.put("MC", "FR");
        veciniTara.put("MD", "RO~UA");
        veciniTara.put("ME", "AL~BA~HR~RS");
        veciniTara.put("MK", "AL~BG~GR~RS");
        veciniTara.put("MT", "");
        veciniTara.put("NL", "BE~DE");
        veciniTara.put("NO", "FI~RU~SE");
        veciniTara.put("PL", "BY~CZ~DE~LT~SK~UA");
        veciniTara.put("PT", "ES");
        veciniTara.put("RO", "BG~HU~MD~RS~UA");
        veciniTara.put("RS", "AL~BA~BG~HR~HU~ME~MK~RO");
        veciniTara.put("RU", "BY~EE~FI~LV~NO~UA");
        veciniTara.put("SE", "FI~NO");
        veciniTara.put("SI", "AT~HR~IT~HU");
        veciniTara.put("SK", "AT~CZ~HU~PL~UA");
        veciniTara.put("SM", "IT");
        veciniTara.put("TR", "BG~GR");
        veciniTara.put("UA", "BY~HU~MD~PL~RO~RU~SK");
        veciniTara.put("UK", "IE");
        veciniTara.put("VA", "IT");

        if (bSOUT || bSOUTFinal)
            System.out.println("Apel din FindRoute()");

        if (bSOUT) {
            System.out.println(veciniTara);

            System.out.println();
            System.out.println("Size:" + veciniTara.size());
            System.out.println("[AD] " + veciniTara.get("AD"));
            System.out.println("[AL] " + veciniTara.get("AL"));
            System.out.println("[AT] " + veciniTara.get("AT"));
            System.out.println("[BA] " + veciniTara.get("BA"));
            System.out.println("[BE] " + veciniTara.get("BE"));
            System.out.println("[BG] " + veciniTara.get("BG"));
            System.out.println("[BY] " + veciniTara.get("BY"));
            System.out.println("[CH] " + veciniTara.get("CH"));
            System.out.println("[CY] " + veciniTara.get("CY"));
            System.out.println("[CZ] " + veciniTara.get("CZ"));
            System.out.println("[DE] " + veciniTara.get("DE"));
            System.out.println("[DK] " + veciniTara.get("DK"));
            System.out.println("[EE] " + veciniTara.get("EE"));
            System.out.println("[ES] " + veciniTara.get("ES"));
            System.out.println("[FI] " + veciniTara.get("FI"));
            System.out.println("[FR] " + veciniTara.get("FR"));
            System.out.println("[GR] " + veciniTara.get("GR"));
            System.out.println("[HR] " + veciniTara.get("HR"));
            System.out.println("[HU] " + veciniTara.get("HU"));
            System.out.println("[IE] " + veciniTara.get("IE"));
            System.out.println("[IS] " + veciniTara.get("IS"));
            System.out.println("[IT] " + veciniTara.get("IT"));
            System.out.println("[LI] " + veciniTara.get("LI"));
            System.out.println("[LT] " + veciniTara.get("LT"));
            System.out.println("[LU] " + veciniTara.get("LU"));
            System.out.println("[LV] " + veciniTara.get("LV"));
            System.out.println("[MC] " + veciniTara.get("MC"));
            System.out.println("[MD] " + veciniTara.get("MD"));
            System.out.println("[ME] " + veciniTara.get("ME"));
            System.out.println("[MK] " + veciniTara.get("MK"));
            System.out.println("[MT] " + veciniTara.get("MT"));
            System.out.println("[NL] " + veciniTara.get("NL"));
            System.out.println("[NO] " + veciniTara.get("NO"));
            System.out.println("[PL] " + veciniTara.get("PL"));
            System.out.println("[PT] " + veciniTara.get("PT"));
            System.out.println("[RO] " + veciniTara.get("RO"));
            System.out.println("[RS] " + veciniTara.get("RS"));
            System.out.println("[RU] " + veciniTara.get("RU"));
            System.out.println("[SE] " + veciniTara.get("SE"));
            System.out.println("[SI] " + veciniTara.get("SI"));
            System.out.println("[SK] " + veciniTara.get("SK"));
            System.out.println("[SM] " + veciniTara.get("SM"));
            System.out.println("[TR] " + veciniTara.get("TR"));
            System.out.println("[UA] " + veciniTara.get("UA"));
            System.out.println("[UK] " + veciniTara.get("UK"));
            System.out.println("[VA] " + veciniTara.get("VA"));
        }

        HashMap<String, ArrayList<String>> veciniTaraArrayList = new HashMap<String, ArrayList<String>>();
        ArrayList arrayList = new ArrayList();

        // AD
        arrayList = new ArrayList();
        arrayList.add("ES");
        arrayList.add("FR");
        veciniTaraArrayList.put("AD", arrayList);

        // AL
        arrayList = new ArrayList();
        arrayList.add("GR");
        arrayList.add("ME");
        arrayList.add("MK");
        arrayList.add("RS");
        veciniTaraArrayList.put("AL", arrayList);

        // AT
        arrayList = new ArrayList();
        arrayList.add("CH");
        arrayList.add("CZ");
        arrayList.add("DE");
        arrayList.add("HU");
        arrayList.add("IT");
        arrayList.add("LI");
        arrayList.add("SK");
        arrayList.add("SI");
        veciniTaraArrayList.put("AT", arrayList);

        // BA
        String[] veciniSplit = veciniTara.get("BA").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("BA", arrayList);

        // BE
        veciniSplit = veciniTara.get("BE").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("BE", arrayList);

        // BG
        veciniSplit = veciniTara.get("BG").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("BG", arrayList);

        // BY
        arrayList = new ArrayList();
        arrayList.add("LT");
        arrayList.add("LV");
        arrayList.add("PL");
        arrayList.add("RU");
        arrayList.add("UA");
        veciniTaraArrayList.put("BY", arrayList);

        // CH
        veciniSplit = veciniTara.get("CH").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("CH", arrayList);

        // CY
        veciniSplit = veciniTara.get("CY").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("CY", arrayList);

        // CZ
        veciniSplit = veciniTara.get("CZ").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("CZ", arrayList);

        // DE
        veciniSplit = veciniTara.get("DE").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("DE", arrayList);

        // DK
        veciniSplit = veciniTara.get("DK").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("DK", arrayList);

        // EE
        veciniSplit = veciniTara.get("EE").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("EE", arrayList);

        // ES
        veciniSplit = veciniTara.get("ES").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("ES", arrayList);

        // FI
        veciniSplit = veciniTara.get("FI").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("FI", arrayList);

        // FR
        veciniSplit = veciniTara.get("FR").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("FR", arrayList);

        // GR
        veciniSplit = veciniTara.get("GR").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("GR", arrayList);

        // HR
        veciniSplit = veciniTara.get("HR").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("HR", arrayList);

        // HU
        veciniSplit = veciniTara.get("HU").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("HU", arrayList);

        // IE
        veciniSplit = veciniTara.get("IE").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("IE", arrayList);

        // IS
        veciniSplit = veciniTara.get("IS").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("IS", arrayList);

        // IT
        veciniSplit = veciniTara.get("IT").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("IT", arrayList);

        // LI
        veciniSplit = veciniTara.get("LI").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("LI", arrayList);

        // LT
        veciniSplit = veciniTara.get("LT").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("LT", arrayList);

        // LU
        veciniSplit = veciniTara.get("LU").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("LU", arrayList);

        // LV
        veciniSplit = veciniTara.get("LV").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("LV", arrayList);

        // MC
        veciniSplit = veciniTara.get("MC").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("MC", arrayList);

        // MD
        veciniSplit = veciniTara.get("MD").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("MD", arrayList);

        // ME
        veciniSplit = veciniTara.get("ME").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("ME", arrayList);

        // MK
        veciniSplit = veciniTara.get("MK").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("MK", arrayList);

        // MT
        veciniSplit = veciniTara.get("MT").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("MT", arrayList);

        // NL
        veciniSplit = veciniTara.get("NL").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("NL", arrayList);

        // NO
        veciniSplit = veciniTara.get("NO").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("NO", arrayList);

        // PL
        veciniSplit = veciniTara.get("PL").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("PL", arrayList);

        // PT
        veciniSplit = veciniTara.get("PT").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("PT", arrayList);

        // RO
        veciniSplit = veciniTara.get("RO").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("RO", arrayList);

        // RS
        veciniSplit = veciniTara.get("RS").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("RS", arrayList);

        // RU
        veciniSplit = veciniTara.get("RU").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("RU", arrayList);

        // SE
        veciniSplit = veciniTara.get("SE").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("SE", arrayList);

        // SI
        veciniSplit = veciniTara.get("SI").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("SI", arrayList);

        // SK
        veciniSplit = veciniTara.get("SK").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("SK", arrayList);

        // SM
        veciniSplit = veciniTara.get("SM").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("SM", arrayList);

        // TR
        veciniSplit = veciniTara.get("TR").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("TR", arrayList);

        // UA
        veciniSplit = veciniTara.get("UA").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("UA", arrayList);

        // UK
        veciniSplit = veciniTara.get("UK").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("UK", arrayList);

        // VA
        veciniSplit = veciniTara.get("VA").split("~");
        arrayList = new ArrayList();
        for (String codTara : veciniSplit) {
            arrayList.add(codTara);
        }
        veciniTaraArrayList.put("VA", arrayList);

        if (bSOUT) {
            System.out.println();
            System.out.println("Size:" + veciniTaraArrayList.size());
            System.out.println("[AD] " + veciniTaraArrayList.get("AD"));
            System.out.println("[AL] " + veciniTaraArrayList.get("AL"));
            System.out.println("[AT] " + veciniTaraArrayList.get("AT"));
            System.out.println("[BA] " + veciniTaraArrayList.get("BA"));
            System.out.println("[BE] " + veciniTaraArrayList.get("BE"));
            System.out.println("[BG] " + veciniTaraArrayList.get("BG"));
            System.out.println("[BY] " + veciniTaraArrayList.get("BY"));
            System.out.println("[CH] " + veciniTaraArrayList.get("CH"));
            System.out.println("[CY] " + veciniTaraArrayList.get("CY"));
            System.out.println("[CZ] " + veciniTaraArrayList.get("CZ"));
            System.out.println("[DE] " + veciniTaraArrayList.get("DE"));
            System.out.println("[DK] " + veciniTaraArrayList.get("DK"));
            System.out.println("[EE] " + veciniTaraArrayList.get("EE"));
            System.out.println("[ES] " + veciniTaraArrayList.get("ES"));
            System.out.println("[FI] " + veciniTaraArrayList.get("FI"));
            System.out.println("[FR] " + veciniTaraArrayList.get("FR"));
            System.out.println("[GR] " + veciniTaraArrayList.get("GR"));
            System.out.println("[HR] " + veciniTaraArrayList.get("HR"));
            System.out.println("[HU] " + veciniTaraArrayList.get("HU"));
            System.out.println("[IE] " + veciniTaraArrayList.get("IE"));
            System.out.println("[IS] " + veciniTaraArrayList.get("IS"));
            System.out.println("[IT] " + veciniTaraArrayList.get("IT"));
            System.out.println("[LI] " + veciniTaraArrayList.get("LI"));
            System.out.println("[LT] " + veciniTaraArrayList.get("LT"));
            System.out.println("[LU] " + veciniTaraArrayList.get("LU"));
            System.out.println("[LV] " + veciniTaraArrayList.get("LV"));
            System.out.println("[MC] " + veciniTaraArrayList.get("MC"));
            System.out.println("[MD] " + veciniTaraArrayList.get("MD"));
            System.out.println("[ME] " + veciniTaraArrayList.get("ME"));
            System.out.println("[MK] " + veciniTaraArrayList.get("MK"));
            System.out.println("[MT] " + veciniTaraArrayList.get("MT"));
            System.out.println("[NL] " + veciniTaraArrayList.get("NL"));
            System.out.println("[NO] " + veciniTaraArrayList.get("NO"));
            System.out.println("[PL] " + veciniTaraArrayList.get("PL"));
            System.out.println("[PT] " + veciniTaraArrayList.get("PT"));
            System.out.println("[RO] " + veciniTaraArrayList.get("RO"));
            System.out.println("[RS] " + veciniTaraArrayList.get("RS"));
            System.out.println("[RU] " + veciniTaraArrayList.get("RU"));
            System.out.println("[SE] " + veciniTaraArrayList.get("SE"));
            System.out.println("[SI] " + veciniTaraArrayList.get("SI"));
            System.out.println("[SK] " + veciniTaraArrayList.get("SK"));
            System.out.println("[SM] " + veciniTaraArrayList.get("SM"));
            System.out.println("[TR] " + veciniTaraArrayList.get("TR"));
            System.out.println("[UA] " + veciniTaraArrayList.get("UA"));
            System.out.println("[UK] " + veciniTaraArrayList.get("UK"));
            System.out.println("[VA] " + veciniTaraArrayList.get("VA"));
        }

        int[][] harta = new int[nrMaxTari][nrMaxTari];
        for (int i = CoduriTari.AD.ordinal(); i <= CoduriTari.VA.ordinal(); i++) {
            for (int j = CoduriTari.AD.ordinal(); j <= CoduriTari.VA.ordinal(); j++) {
                harta[i][j] = 0;
            }
        }

        // AD
        harta[CoduriTari.AD.ordinal()][CoduriTari.ES.ordinal()] = 1;
        harta[CoduriTari.AD.ordinal()][CoduriTari.FR.ordinal()] = 1;

        // AL
        harta[CoduriTari.AL.ordinal()][CoduriTari.GR.ordinal()] = 1;
        harta[CoduriTari.AL.ordinal()][CoduriTari.ME.ordinal()] = 1;
        harta[CoduriTari.AL.ordinal()][CoduriTari.MK.ordinal()] = 1;
        harta[CoduriTari.AL.ordinal()][CoduriTari.RS.ordinal()] = 1;

        // AT
        harta[CoduriTari.AT.ordinal()][CoduriTari.CH.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.CZ.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.IT.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.LI.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.SK.ordinal()] = 1;
        harta[CoduriTari.AT.ordinal()][CoduriTari.SI.ordinal()] = 1;

        // BA
        harta[CoduriTari.BA.ordinal()][CoduriTari.HR.ordinal()] = 1;
        harta[CoduriTari.BA.ordinal()][CoduriTari.ME.ordinal()] = 1;
        harta[CoduriTari.BA.ordinal()][CoduriTari.RS.ordinal()] = 1;

        // BE
        harta[CoduriTari.BE.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.BE.ordinal()][CoduriTari.FR.ordinal()] = 1;
        harta[CoduriTari.BE.ordinal()][CoduriTari.LU.ordinal()] = 1;
        harta[CoduriTari.BE.ordinal()][CoduriTari.NL.ordinal()] = 1;

        // BG
        harta[CoduriTari.BG.ordinal()][CoduriTari.GR.ordinal()] = 1;
        harta[CoduriTari.BG.ordinal()][CoduriTari.MK.ordinal()] = 1;
        harta[CoduriTari.BG.ordinal()][CoduriTari.RO.ordinal()] = 1;
        harta[CoduriTari.BG.ordinal()][CoduriTari.RS.ordinal()] = 1;
        harta[CoduriTari.BG.ordinal()][CoduriTari.TR.ordinal()] = 1;

        // BY
        harta[CoduriTari.BY.ordinal()][CoduriTari.LT.ordinal()] = 1;
        harta[CoduriTari.BY.ordinal()][CoduriTari.LV.ordinal()] = 1;
        harta[CoduriTari.BY.ordinal()][CoduriTari.PL.ordinal()] = 1;
        harta[CoduriTari.BY.ordinal()][CoduriTari.RU.ordinal()] = 1;
        harta[CoduriTari.BY.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // CH
        harta[CoduriTari.CH.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.CH.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.CH.ordinal()][CoduriTari.FR.ordinal()] = 1;
        harta[CoduriTari.CH.ordinal()][CoduriTari.LI.ordinal()] = 1;
        harta[CoduriTari.CH.ordinal()][CoduriTari.IT.ordinal()] = 1;

        // CY: null

        // CZ
        harta[CoduriTari.CZ.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.CZ.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.CZ.ordinal()][CoduriTari.PL.ordinal()] = 1;
        harta[CoduriTari.CZ.ordinal()][CoduriTari.SK.ordinal()] = 1;

        // DE
        harta[CoduriTari.DE.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.BE.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.CH.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.CZ.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.DK.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.FR.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.LU.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.NL.ordinal()] = 1;
        harta[CoduriTari.DE.ordinal()][CoduriTari.PL.ordinal()] = 1;

        // DK
        harta[CoduriTari.DK.ordinal()][CoduriTari.DE.ordinal()] = 1;

        // EE
        harta[CoduriTari.EE.ordinal()][CoduriTari.LV.ordinal()] = 1;
        harta[CoduriTari.EE.ordinal()][CoduriTari.RU.ordinal()] = 1;

        // ES
        harta[CoduriTari.ES.ordinal()][CoduriTari.AD.ordinal()] = 1;
        harta[CoduriTari.ES.ordinal()][CoduriTari.FR.ordinal()] = 1;
        harta[CoduriTari.ES.ordinal()][CoduriTari.PT.ordinal()] = 1;

        // FI
        harta[CoduriTari.FI.ordinal()][CoduriTari.NO.ordinal()] = 1;
        harta[CoduriTari.FI.ordinal()][CoduriTari.RU.ordinal()] = 1;
        harta[CoduriTari.FI.ordinal()][CoduriTari.SE.ordinal()] = 1;

        // FR
        harta[CoduriTari.FR.ordinal()][CoduriTari.AD.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.BE.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.CH.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.ES.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.IT.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.LU.ordinal()] = 1;
        harta[CoduriTari.FR.ordinal()][CoduriTari.MC.ordinal()] = 1;

        // GR
        harta[CoduriTari.GR.ordinal()][CoduriTari.AL.ordinal()] = 1;
        harta[CoduriTari.GR.ordinal()][CoduriTari.BG.ordinal()] = 1;
        harta[CoduriTari.GR.ordinal()][CoduriTari.MK.ordinal()] = 1;
        harta[CoduriTari.GR.ordinal()][CoduriTari.TR.ordinal()] = 1;

        // HR
        harta[CoduriTari.HR.ordinal()][CoduriTari.BA.ordinal()] = 1;
        harta[CoduriTari.HR.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.HR.ordinal()][CoduriTari.ME.ordinal()] = 1;
        harta[CoduriTari.HR.ordinal()][CoduriTari.RS.ordinal()] = 1;
        harta[CoduriTari.HR.ordinal()][CoduriTari.SI.ordinal()] = 1;

        // HU
        harta[CoduriTari.HU.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.HR.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.RO.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.RS.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.SK.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.SI.ordinal()] = 1;
        harta[CoduriTari.HU.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // IE
        harta[CoduriTari.IE.ordinal()][CoduriTari.UK.ordinal()] = 1;

        // IS => null

        // IT
        harta[CoduriTari.IT.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.IT.ordinal()][CoduriTari.CH.ordinal()] = 1;
        harta[CoduriTari.IT.ordinal()][CoduriTari.FR.ordinal()] = 1;
        harta[CoduriTari.IT.ordinal()][CoduriTari.SI.ordinal()] = 1;
        harta[CoduriTari.IT.ordinal()][CoduriTari.SM.ordinal()] = 1;
        harta[CoduriTari.IT.ordinal()][CoduriTari.VA.ordinal()] = 1;

        // LI
        harta[CoduriTari.LI.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.LI.ordinal()][CoduriTari.CH.ordinal()] = 1;

        // LT
        harta[CoduriTari.LT.ordinal()][CoduriTari.BY.ordinal()] = 1;
        harta[CoduriTari.LT.ordinal()][CoduriTari.LV.ordinal()] = 1;
        harta[CoduriTari.LT.ordinal()][CoduriTari.PL.ordinal()] = 1;

        // LU
        harta[CoduriTari.LU.ordinal()][CoduriTari.BE.ordinal()] = 1;
        harta[CoduriTari.LU.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.LU.ordinal()][CoduriTari.FR.ordinal()] = 1;

        // LV
        harta[CoduriTari.LV.ordinal()][CoduriTari.BY.ordinal()] = 1;
        harta[CoduriTari.LV.ordinal()][CoduriTari.EE.ordinal()] = 1;
        harta[CoduriTari.LV.ordinal()][CoduriTari.LT.ordinal()] = 1;
        harta[CoduriTari.LV.ordinal()][CoduriTari.RU.ordinal()] = 1;

        // MC
        harta[CoduriTari.MC.ordinal()][CoduriTari.FR.ordinal()] = 1;

        // MD
        harta[CoduriTari.MD.ordinal()][CoduriTari.RO.ordinal()] = 1;
        harta[CoduriTari.MD.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // ME
        harta[CoduriTari.ME.ordinal()][CoduriTari.AL.ordinal()] = 1;
        harta[CoduriTari.ME.ordinal()][CoduriTari.BA.ordinal()] = 1;
        harta[CoduriTari.ME.ordinal()][CoduriTari.HR.ordinal()] = 1;
        harta[CoduriTari.ME.ordinal()][CoduriTari.RS.ordinal()] = 1;

        // MK
        harta[CoduriTari.MK.ordinal()][CoduriTari.AL.ordinal()] = 1;
        harta[CoduriTari.MK.ordinal()][CoduriTari.BG.ordinal()] = 1;
        harta[CoduriTari.MK.ordinal()][CoduriTari.GR.ordinal()] = 1;
        harta[CoduriTari.MK.ordinal()][CoduriTari.RS.ordinal()] = 1;

        // MT => null

        // NL
        harta[CoduriTari.NL.ordinal()][CoduriTari.BE.ordinal()] = 1;
        harta[CoduriTari.NL.ordinal()][CoduriTari.DE.ordinal()] = 1;

        // NO
        harta[CoduriTari.NO.ordinal()][CoduriTari.FI.ordinal()] = 1;
        harta[CoduriTari.NO.ordinal()][CoduriTari.RU.ordinal()] = 1;
        harta[CoduriTari.NO.ordinal()][CoduriTari.SE.ordinal()] = 1;

        // PL
        harta[CoduriTari.PL.ordinal()][CoduriTari.BY.ordinal()] = 1;
        harta[CoduriTari.PL.ordinal()][CoduriTari.CZ.ordinal()] = 1;
        harta[CoduriTari.PL.ordinal()][CoduriTari.DE.ordinal()] = 1;
        harta[CoduriTari.PL.ordinal()][CoduriTari.LT.ordinal()] = 1;
        harta[CoduriTari.PL.ordinal()][CoduriTari.SK.ordinal()] = 1;
        harta[CoduriTari.PL.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // PT
        harta[CoduriTari.PT.ordinal()][CoduriTari.ES.ordinal()] = 1;

        // RO
        harta[CoduriTari.RO.ordinal()][CoduriTari.BG.ordinal()] = 1;
        harta[CoduriTari.RO.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.RO.ordinal()][CoduriTari.MD.ordinal()] = 1;
        harta[CoduriTari.RO.ordinal()][CoduriTari.RS.ordinal()] = 1;
        harta[CoduriTari.RO.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // RS
        harta[CoduriTari.RS.ordinal()][CoduriTari.AL.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.BA.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.BG.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.HR.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.ME.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.MK.ordinal()] = 1;
        harta[CoduriTari.RS.ordinal()][CoduriTari.RO.ordinal()] = 1;

        // RU
        harta[CoduriTari.RU.ordinal()][CoduriTari.BY.ordinal()] = 1;
        harta[CoduriTari.RU.ordinal()][CoduriTari.EE.ordinal()] = 1;
        harta[CoduriTari.RU.ordinal()][CoduriTari.FI.ordinal()] = 1;
        harta[CoduriTari.RU.ordinal()][CoduriTari.LV.ordinal()] = 1;
        harta[CoduriTari.RU.ordinal()][CoduriTari.NO.ordinal()] = 1;
        harta[CoduriTari.RU.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // SE
        harta[CoduriTari.SE.ordinal()][CoduriTari.FI.ordinal()] = 1;
        harta[CoduriTari.SE.ordinal()][CoduriTari.NO.ordinal()] = 1;

        // SI
        harta[CoduriTari.SI.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.SI.ordinal()][CoduriTari.HR.ordinal()] = 1;
        harta[CoduriTari.SI.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.SI.ordinal()][CoduriTari.IT.ordinal()] = 1;

        // SK
        harta[CoduriTari.SK.ordinal()][CoduriTari.AT.ordinal()] = 1;
        harta[CoduriTari.SK.ordinal()][CoduriTari.CZ.ordinal()] = 1;
        harta[CoduriTari.SK.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.SK.ordinal()][CoduriTari.PL.ordinal()] = 1;
        harta[CoduriTari.SK.ordinal()][CoduriTari.UA.ordinal()] = 1;

        // SM
        harta[CoduriTari.SM.ordinal()][CoduriTari.IT.ordinal()] = 1;

        // TR
        harta[CoduriTari.TR.ordinal()][CoduriTari.BG.ordinal()] = 1;
        harta[CoduriTari.TR.ordinal()][CoduriTari.GR.ordinal()] = 1;

        // UA
        harta[CoduriTari.UA.ordinal()][CoduriTari.BY.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.HU.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.MD.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.PL.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.RO.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.RU.ordinal()] = 1;
        harta[CoduriTari.UA.ordinal()][CoduriTari.SK.ordinal()] = 1;

        // UK
        harta[CoduriTari.UK.ordinal()][CoduriTari.IE.ordinal()] = 1;

        // VA
        harta[CoduriTari.VA.ordinal()][CoduriTari.IT.ordinal()] = 1;

        //
        if (bSOUT) {
            for (int i = CoduriTari.AD.ordinal(); i <= CoduriTari.VA.ordinal(); i++) {
                System.out.print(CoduriTari.values()[i] + " ");
                for (int j = CoduriTari.AD.ordinal(); j <= CoduriTari.VA.ordinal(); j++) {
                    System.out.print((harta[i][j] == 0 ? "_" : "1") + " ");
                }
                System.out.println();
            }
        }

        // Exista drum de la '" + Start + "' la '" + End + "'?
        //CoduriTari StartUnic = CoduriTari.PT;
        //CoduriTari EndUnic   = CoduriTari.ES;
        CoduriTari StartUnic = CoduriTari.values()[nIndexStartCode];
        CoduriTari EndUnic = CoduriTari.values()[nIndexEndCode];

        CoduriTari Start = StartUnic;
        CoduriTari End = EndUnic;

        boolean exista = false;
        int len = 0;
        String drum = "";
        String sDrumuri = "";
        String sSep = "-";

        //boolean bLoopToateTarile = true; // Loop pentru una sau mai multe tari

        int startIndexLoop = 0;
        int endIndexLoop = 0;

        if (bLoopToateTarile) {
            endIndexLoop = nrMaxTari - 1;
        }

        for (int iLoop = startIndexLoop; iLoop <= endIndexLoop; iLoop++) {

            if (bLoopToateTarile) {
                End = CoduriTari.values()[iLoop];
            }

            if (Start.ordinal() == End.ordinal()) {
                if (bSOUTFinal) {
                    System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "': este acelasi cod de tara.");
                    System.out.println();
                }
                continue;
            }

            exista = false;
            len = 0;
            drum = "";
            sDrumuri = "";

            nrDrumuri1 = 0;
            nrDrumuri2 = 0;
            nrDrumuri3 = 0;
            nrDrumuri4 = 0;
            nrDrumuri5 = 0;
            nrDrumuri6 = 0;
            nrDrumuri7 = 0;
            nrDrumuri8 = 0;
            nrDrumuri9 = 0;
            nrDrumuri10 = 0;

            if (harta[Start.ordinal()][End.ordinal()] == 1) { // lungime = 1
                exista = true;
                len = 1;
                nrDrumuri1 = 1;
                drum = Start + sSep + End;
                if (sDrumuri.length() == 0)
                    sDrumuri = drum;
                else
                    sDrumuri += ";" + drum;
                if (bSOUTFinal)
                    System.out.println("(" + nrDrumuri1 + ") Exista drum direct de la '" + Start + "' la '" + End + "': '" + drum + "'");
            } else { // lungime > 1

                // lungime = 2
                for (int i = 0; i < nrMaxTari; i++) {
                    if (harta[Start.ordinal()][i] == 0) {
                        continue;
                    }
                    if ((harta[Start.ordinal()][i] == 1) && (harta[i][End.ordinal()] == 1)) {
                        exista = true;
                        len = 2;
                        nrDrumuri2++;
                        drum = Start + sSep + CoduriTari.values()[i] + sSep + End;
                        if (sDrumuri.length() == 0)
                            sDrumuri = drum;
                        else
                            sDrumuri += ";" + drum;
                        if (bSOUTFinal)
                            System.out.println("(" + nrDrumuri2 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                        //break;
                    }
                }

                // lungime = 3
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][End.ordinal()] == 1)) {
                                exista = true;
                                len = 3;
                                nrDrumuri3++;
                                drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + End;
                                if (sDrumuri.length() == 0)
                                    sDrumuri = drum;
                                else
                                    sDrumuri += ";" + drum;
                                if (bSOUTFinal)
                                    System.out.println("(" + nrDrumuri3 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                //break;
                            }
                            //if (exista) {
                            //     break;
                            //}
                        }
                    }
                }

                // lungime = 4
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1) && (harta[k][End.ordinal()] == 1)) {
                                    exista = true;
                                    len = 4;
                                    nrDrumuri4++;
                                    drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k] + sSep + End;
                                    if (sDrumuri.length() == 0)
                                        sDrumuri = drum;
                                    else
                                        sDrumuri += ";" + drum;
                                    if (bSOUTFinal)
                                        System.out.println("(" + nrDrumuri4 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                    //break;
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //     break;
                            //}
                        }
                    }
                }

                // lungime = 5
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }
                                    if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1)
                                            && (harta[k][l] == 1) && (harta[l][End.ordinal()] == 1)) {
                                        exista = true;
                                        len = 5;
                                        nrDrumuri5++;
                                        drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep
                                                + CoduriTari.values()[k] + sSep + CoduriTari.values()[l] + sSep + End;
                                        if (sDrumuri.length() == 0)
                                            sDrumuri = drum;
                                        else
                                            sDrumuri += ";" + drum;
                                        if (bSOUTFinal)
                                            System.out.println("(" + nrDrumuri5 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                        //break;
                                    }
                                    //if (exista) {
                                    //     break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }

                // lungime = 6
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }
                                    for (int m = 0; m < nrMaxTari; m++) {
                                        if (harta[l][m] == 0) {
                                            continue;
                                        }
                                        if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1)
                                                && (harta[k][l] == 1) && (harta[l][m] == 1) && (harta[m][End.ordinal()] == 1)) {
                                            exista = true;
                                            len = 6;
                                            nrDrumuri6++;
                                            drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k]
                                                    + sSep + CoduriTari.values()[l] + sSep + CoduriTari.values()[m] + sSep + End;
                                            if (sDrumuri.length() == 0)
                                                sDrumuri = drum;
                                            else
                                                sDrumuri += ";" + drum;
                                            if (bSOUTFinal)
                                                System.out.println("(" + nrDrumuri6 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                            //break;
                                        }
                                        //if (exista) {
                                        //    break;
                                        //}
                                    }
                                    //if (exista) {
                                    //      break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }

                // lungime = 7
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }
                                    for (int m = 0; m < nrMaxTari; m++) {
                                        if (harta[l][m] == 0) {
                                            continue;
                                        }
                                        for (int n = 0; n < nrMaxTari; n++) {
                                            if (harta[m][n] == 0) {
                                                continue;
                                            }
                                            if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1) && (harta[k][l] == 1)
                                                    && (harta[l][m] == 1) && (harta[m][n] == 1) && (harta[n][End.ordinal()] == 1)) {
                                                exista = true;
                                                len = 7;
                                                nrDrumuri7++;
                                                drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k]
                                                        + sSep + CoduriTari.values()[l] + sSep + CoduriTari.values()[m] + sSep + CoduriTari.values()[n] + sSep + End;
                                                if (sDrumuri.length() == 0)
                                                    sDrumuri = drum;
                                                else
                                                    sDrumuri += ";" + drum;
                                                if (bSOUTFinal)
                                                    System.out.println("(" + nrDrumuri7 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                                //break;
                                            }
                                            //if (exista) {
                                            //    break;
                                            //}
                                        }
                                        //if (exista) {
                                        //    break;
                                        //}
                                    }
                                    //if (exista) {
                                    //      break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }

                // lungime = 8
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }
                                    for (int m = 0; m < nrMaxTari; m++) {
                                        if (harta[l][m] == 0) {
                                            continue;
                                        }
                                        for (int n = 0; n < nrMaxTari; n++) {
                                            if (harta[m][n] == 0) {
                                                continue;
                                            }
                                            for (int p = 0; p < nrMaxTari; p++) {
                                                if (harta[n][p] == 0) {
                                                    continue;
                                                }
                                                if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1) && (harta[k][l] == 1)
                                                        && (harta[l][m] == 1) && (harta[m][n] == 1) && (harta[n][p] == 1) && (harta[p][End.ordinal()] == 1)) {
                                                    exista = true;
                                                    len = 8;
                                                    nrDrumuri8++;
                                                    drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k]
                                                            + sSep + CoduriTari.values()[l] + sSep + CoduriTari.values()[m] + sSep
                                                            + CoduriTari.values()[n] + sSep + CoduriTari.values()[p] + sSep + End;
                                                    if (sDrumuri.length() == 0)
                                                        sDrumuri = drum;
                                                    else
                                                        sDrumuri += ";" + drum;
                                                    if (bSOUTFinal)
                                                        System.out.println("(" + nrDrumuri8 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                                    //break;
                                                }
                                                //if (exista) {
                                                //    break;
                                                //}
                                            }
                                            //if (exista) {
                                            //    break;
                                            //}
                                        }
                                        //if (exista) {
                                        //    break;
                                        //}
                                    }
                                    //if (exista) {
                                    //      break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }

                // lungime = 9
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }
                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }
                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }
                                    for (int m = 0; m < nrMaxTari; m++) {
                                        if (harta[l][m] == 0) {
                                            continue;
                                        }
                                        for (int n = 0; n < nrMaxTari; n++) {
                                            if (harta[m][n] == 0) {
                                                continue;
                                            }
                                            for (int p = 0; p < nrMaxTari; p++) {
                                                if (harta[n][p] == 0) {
                                                    continue;
                                                }
                                                for (int q = 0; q < nrMaxTari; q++) {
                                                    if (harta[p][q] == 0) {
                                                        continue;
                                                    }
                                                    if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1) && (harta[k][l] == 1) && (harta[l][m] == 1)
                                                            && (harta[m][n] == 1) && (harta[n][p] == 1) && (harta[p][q] == 1) && (harta[q][End.ordinal()] == 1)) {
                                                        exista = true;
                                                        len = 9;
                                                        nrDrumuri9++;
                                                        drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k]
                                                                + sSep + CoduriTari.values()[l] + sSep + CoduriTari.values()[m] + sSep
                                                                + CoduriTari.values()[n] + sSep + CoduriTari.values()[p] + sSep + CoduriTari.values()[q] + sSep + End;
                                                        if (sDrumuri.length() == 0)
                                                            sDrumuri = drum;
                                                        else
                                                            sDrumuri += ";" + drum;
                                                        if (bSOUTFinal)
                                                            System.out.println("(" + nrDrumuri9 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                                        //break;
                                                    }
                                                    //if (exista) {
                                                    //     break;
                                                    //}
                                                }
                                                //if (exista) {
                                                //    break;
                                                //}
                                            }
                                            //if (exista) {
                                            //    break;
                                            //}
                                        }
                                        //if (exista) {
                                        //    break;
                                        //}
                                    }
                                    //if (exista) {
                                    //      break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }

                // lungime = 10
                //String codes = "";
                if (!exista) {
                    for (int i = 0; i < nrMaxTari; i++) {
                        if (harta[Start.ordinal()][i] == 0) {
                            continue;
                        }
                        for (int j = 0; j < nrMaxTari; j++) {
                            if (harta[i][j] == 0) {
                                continue;
                            }

                            for (int k = 0; k < nrMaxTari; k++) {
                                if (harta[j][k] == 0) {
                                    continue;
                                }

                                for (int l = 0; l < nrMaxTari; l++) {
                                    if (harta[k][l] == 0) {
                                        continue;
                                    }

                                    for (int m = 0; m < nrMaxTari; m++) {
                                        if (harta[l][m] == 0) {
                                            continue;
                                        }

                                        for (int n = 0; n < nrMaxTari; n++) {
                                            if (harta[m][n] == 0) {
                                                continue;
                                            }

                                            for (int p = 0; p < nrMaxTari; p++) {
                                                if (harta[n][p] == 0) {
                                                    continue;
                                                }

                                                for (int q = 0; q < nrMaxTari; q++) {
                                                    if (harta[p][q] == 0) {
                                                        continue;
                                                    }

                                                    for (int r = 0; r < nrMaxTari; r++) {
                                                        if (harta[q][r] == 0) {
                                                            continue;
                                                        }

                                                        if ((harta[Start.ordinal()][i] == 1) && (harta[i][j] == 1) && (harta[j][k] == 1) && (harta[k][l] == 1) && (harta[l][m] == 1)
                                                                && (harta[m][n] == 1) && (harta[n][p] == 1) && (harta[p][q] == 1) && (harta[q][r] == 1) && (harta[r][End.ordinal()] == 1)) {
                                                            exista = true;
                                                            len = 10;
                                                            nrDrumuri10++;
                                                            drum = Start + sSep + CoduriTari.values()[i] + sSep + CoduriTari.values()[j] + sSep + CoduriTari.values()[k]
                                                                    + sSep + CoduriTari.values()[l] + sSep + CoduriTari.values()[m] + sSep + CoduriTari.values()[n]
                                                                    + sSep + CoduriTari.values()[p] + sSep + CoduriTari.values()[q] + sSep + CoduriTari.values()[r] + sSep + End;
                                                            if (sDrumuri.length() == 0)
                                                                sDrumuri = drum;
                                                            else
                                                                sDrumuri += ";" + drum;
                                                            if (bSOUT)
                                                                System.out.println("(" + nrDrumuri10 + ") Exista drum de la '" + Start + "' la '" + End + "' cu lungimea " + len + ": '" + drum + "'");
                                                            //break;
                                                        }
                                                        //if (exista) {
                                                        //     break;
                                                        //}
                                                    }
                                                    //if (exista) {
                                                    //     break;
                                                    //}
                                                }
                                                //if (exista) {
                                                //    break;
                                                //}
                                            }
                                            //if (exista) {
                                            //    break;
                                            //}
                                        }
                                        //if (exista) {
                                        //    break;
                                        //}
                                    }
                                    //if (exista) {
                                    //      break;
                                    //}
                                }
                                //if (exista) {
                                //     break;
                                //}
                            }
                            //if (exista) {
                            //    break;
                            //}
                        }
                    }
                }
            }

            if (bSOUTFinal) {
                if (nrDrumuri1 > 0) {
                    System.out.println("Exista " + nrDrumuri1 + " drum" + (nrDrumuri1 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 1.");
                    nrDrumuri = nrDrumuri1;
                    nLungimeDrum = 1;
                } else if (nrDrumuri2 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    System.out.println("Exista " + nrDrumuri2 + " drum" + (nrDrumuri2 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 2.");
                    nrDrumuri = nrDrumuri2;
                    nLungimeDrum = 2;
                } else if (nrDrumuri3 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    System.out.println("Exista " + nrDrumuri3 + " drum" + (nrDrumuri3 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 3.");
                    nrDrumuri = nrDrumuri3;
                    nLungimeDrum = 3;
                } else if (nrDrumuri4 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    System.out.println("Exista " + nrDrumuri4 + " drum" + (nrDrumuri4 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 4.");
                    nrDrumuri = nrDrumuri4;
                    nLungimeDrum = 4;
                } else if (nrDrumuri5 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    System.out.println("Exista " + nrDrumuri5 + " drum" + (nrDrumuri5 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 5.");
                    nrDrumuri = nrDrumuri5;
                    nLungimeDrum = 5;
                } else if (nrDrumuri6 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    System.out.println("Exista " + nrDrumuri6 + " drum" + (nrDrumuri6 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 6.");
                    nrDrumuri = nrDrumuri6;
                    nLungimeDrum = 6;
                } else if (nrDrumuri7 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 6.");
                    System.out.println("Exista " + nrDrumuri7 + " drum" + (nrDrumuri7 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 7.");
                    nrDrumuri = nrDrumuri7;
                    nLungimeDrum = 7;
                } else if (nrDrumuri8 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 6.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 7.");
                    System.out.println("Exista " + nrDrumuri8 + " drum" + (nrDrumuri8 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 8.");
                    nrDrumuri = nrDrumuri8;
                    nLungimeDrum = 8;
                } else if (nrDrumuri9 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 6.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 7.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 8.");
                    System.out.println("Exista " + nrDrumuri9 + " drum" + (nrDrumuri9 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 9.");
                    nrDrumuri = nrDrumuri9;
                    nLungimeDrum = 9;
                } else if (nrDrumuri10 > 0) {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 6.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 7.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 8.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 9.");
                    System.out.println("Exista " + nrDrumuri10 + " drum" + (nrDrumuri10 > 1 ? "uri" : "") + " de la '" + Start + "' la '" + End + "' de lungime 10.");
                    nrDrumuri = nrDrumuri10;
                    nLungimeDrum = 10;
                } else {
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 1.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 2.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 3.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 4.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 5.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 6.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 7.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 8.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 9.");
                    //System.out.println("Nu exista drumuri de la '" + Start + "' la '" + End + "' de lungime 10.");
                    System.out.println("Nu exista drum de la '" + Start + "' la '" + End + "'!");
                    nrDrumuri = 0;
                    nLungimeDrum = 0;
                }
            }

            if (nrDrumuri > 0) {
                if (nrDrumuri == 1)
                    System.out.println("Drumul este:");
                else
                    System.out.println("Drumurile sunt:");
                System.out.println("'" + sDrumuri + "'");
                System.out.println();
            }
        }

        if (nrDrumuri > 0) {
            if (bGetExtendedCode) {
                sDrumuri = ExtendCodes(sDrumuri);
            }
        } else {
            if (bGetExtendedCode) {
                sDrumuri = "There is No Route between '" + GetCountryName(sStartCode.toLowerCase()) + "' and '" + GetCountryName(sEndCode.toLowerCase()) + "'!";
            }
            else {
                sDrumuri = "There is No Route between '" + Start + "' and '" + End + "'!";
            }
        }

        return sDrumuri;
    }

    public String ExtendCodes(String sDrum) {
        String sLocalDrum = sDrum;
        if (sLocalDrum.length() == 0)
            return "";

        sLocalDrum = sLocalDrum.trim();
        if (sLocalDrum.length() == 0)
            return "";

        sLocalDrum = sLocalDrum.toLowerCase();

        String sExtendedDrum = "";
        String[] drumuriSplit = sLocalDrum.split(";");
        String[] coduriSplit;

        for (int i = 0; i < drumuriSplit.length; i++) {
            coduriSplit = drumuriSplit[i].split("-");
            for (int j = 0; j < coduriSplit.length; j++) {
                sExtendedDrum += GetCountryName(coduriSplit[j]) + (j < coduriSplit.length - 1 ? "-" : "");
            }
            sExtendedDrum += (i < drumuriSplit.length - 1 ? ";" : "");
        }
        return sExtendedDrum;
    }
    public String GetCountryName(String sCode) {

        switch (sCode) {
            case "al":
                return "Albania";
            case "ad":
                return "Andorra";
            case "at":
                return "Austria";
            case "by":
                return "Belarus";
            case "be":
                return "Belgium";
            case "ba":
                return "Bosnia and Herzegovina";
            case "bg":
                return "Bulgaria";
            case "hr":
                return "Croatia";
            case "cy":
                return "Cyprus";
            case "cz":
                return "Czechia";
            case "dk":
                return "Denmark";
            case "ee":
                return "Estonia";
            case "fi":
                return "Finland";
            case "fr":
                return "France";
            case "de":
                return "Germany";
            case "gr":
                return "Greece";
            case "hu":
                return "Hungary";
            case "is":
                return "Iceland";
            case "ie":
                return "Ireland";
            case "it":
                return "Italy";
            case "lv":
                return "Latvia";
            case "li":
                return "Liechtenstein";
            case "lt":
                return "Lithuania";
            case "lu":
                return "Luxembourg";
            case "mt":
                return "Malta";
            case "md":
                return "Moldova";
            case "mc":
                return "Monaco";
            case "me":
                return "Montenegro";
            case "nl":
                return "Netherlands";
            case "mk":
                return "North Macedonia";
            case "no":
                return "Norway";
            case "pl":
                return "Poland";
            case "pt":
                return "Portugal";
            case "ro":
                return "Romania";
            case "ru":
                return "Russia";
            case "sm":
                return "San Marino";
            case "rs":
                return "Serbia";
            case "sk":
                return "Slovakia";
            case "si":
                return "Slovenia";
            case "es":
                return "Spain";
            case "se":
                return "Sweden";
            case "ch":
                return "Switzerland";
            case "tr":
                return "Turkey";
            case "ua":
                return "Ukraine";
            case "uk":
                return "United Kingdom";
            case "va":
                return "Vatican";
            default:
                return "";
        }
    }
}
