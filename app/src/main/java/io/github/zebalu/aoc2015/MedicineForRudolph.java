package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class MedicineForRudolph {

    public static void main(String[] args) {
        var machine = new ConversionMachine(readInput());
        System.out.println(machine.countAllPossibleOneStepMols());
        System.out.println(machine.shortestPastFromMedicine());
    }

    private static final class ConversionMachine {
        Map<String, Set<String>> rules = new HashMap<>();
        String startMol;

        public ConversionMachine(List<String> description) {
            boolean switchFound = false;
            Iterator<String> steps = description.iterator();
            while (steps.hasNext() && !switchFound) {
                String step = steps.next();
                if (step.trim().isEmpty()) {
                    switchFound = true;
                } else {
                    var kv = step.split(" => ");
                    rules.computeIfAbsent(kv[0], k -> new HashSet<>()).add(kv[1]);
                }
            }
            startMol = steps.next();
        }

        int countAllPossibleOneStepMols() {
            return nextMols(startMol).size();
        }

        Set<String> nextMols(String startMol) {
            return nextMols(startMol, rules);
        }

        Set<String> nextMols(String startMol, Map<String, Set<String>> rules) {
            Set<String> mols = new HashSet<>();
            rules.keySet().forEach(key -> {
                int start = 0;
                while (start != -1) {
                    start = startMol.indexOf(key, start);
                    if (start > -1) {
                        String before = start == 0 ? "" : startMol.substring(0, start);
                        String after = start + key.length() < startMol.length()
                                ? startMol.substring(start + key.length())
                                : "";
                        rules.get(key).forEach(replacement -> mols.add(before + replacement + after));
                        start += 1;
                    }
                }
            });
            return mols;
        }

        int shortestPastFromMedicine() {
            Map<String, Set<String>> reversed = getReversedRules();
            Queue<MolSteps> queue = new PriorityQueue<>(Comparator.comparingInt(ms -> ms.mol().length()));
            Set<String> seen = new HashSet<>();
            seen.add(startMol);
            queue.add(new MolSteps(startMol, 0));
            while (!queue.isEmpty() && !queue.peek().mol().equals("e")) {
                MolSteps top = queue.poll();
                nextMols(top.mol(), reversed).forEach(mol -> {
                    if (!seen.contains(mol)) {
                        queue.add(new MolSteps(mol, top.steps() + 1));
                        seen.add(mol);
                    }
                });
            }
            return queue.peek().steps;
        }

        private Map<String, Set<String>> getReversedRules() {
            Map<String, Set<String>> reversed = new HashMap<>();
            rules.entrySet().forEach(e -> {
                e.getValue().forEach(v -> {
                    reversed.computeIfAbsent(v, k -> new HashSet<>()).add(e.getKey());
                });
            });
            return reversed;
        }

        static record MolSteps(String mol, int steps) {
        }

    }

    private static List<String> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(MedicineForRudolph.class.getResourceAsStream("/day19.txt")))) {
            return reader.lines().toList();
        } catch (Exception e) {
            return INPUT.lines().toList();
        }
    }

    private static final String INPUT = """
            Al => ThF
            Al => ThRnFAr
            B => BCa
            B => TiB
            B => TiRnFAr
            Ca => CaCa
            Ca => PB
            Ca => PRnFAr
            Ca => SiRnFYFAr
            Ca => SiRnMgAr
            Ca => SiTh
            F => CaF
            F => PMg
            F => SiAl
            H => CRnAlAr
            H => CRnFYFYFAr
            H => CRnFYMgAr
            H => CRnMgYFAr
            H => HCa
            H => NRnFYFAr
            H => NRnMgAr
            H => NTh
            H => OB
            H => ORnFAr
            Mg => BF
            Mg => TiMg
            N => CRnFAr
            N => HSi
            O => CRnFYFAr
            O => CRnMgAr
            O => HP
            O => NRnFAr
            O => OTi
            P => CaP
            P => PTi
            P => SiRnFAr
            Si => CaSi
            Th => ThCa
            Ti => BP
            Ti => TiTi
            e => HF
            e => NAl
            e => OMg

            CRnCaSiRnBSiRnFArTiBPTiTiBFArPBCaSiThSiRnTiBPBPMgArCaSiRnTiMgArCaSiThCaSiRnFArRnSiRnFArTiTiBFArCaCaSiRnSiThCaCaSiRnMgArFYSiRnFYCaFArSiThCaSiThPBPTiMgArCaPRnSiAlArPBCaCaSiRnFYSiThCaRnFArArCaCaSiRnPBSiRnFArMgYCaCaCaCaSiThCaCaSiAlArCaCaSiRnPBSiAlArBCaCaCaCaSiThCaPBSiThPBPBCaSiRnFYFArSiThCaSiRnFArBCaCaSiRnFYFArSiThCaPBSiThCaSiRnPMgArRnFArPTiBCaPRnFArCaCaCaCaSiRnCaCaSiRnFYFArFArBCaSiThFArThSiThSiRnTiRnPMgArFArCaSiThCaPBCaSiRnBFArCaCaPRnCaCaPMgArSiRnFYFArCaSiThRnPBPMgAr""";

}
