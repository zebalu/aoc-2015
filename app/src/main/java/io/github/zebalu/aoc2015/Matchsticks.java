package io.github.zebalu.aoc2015;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Matchsticks {

    private static final Set<Character> TO_ESCAPE = Set.of('\\', '"');

    public static void main(String[] args) {
        firstPart();
        secondPart();
    }

    private static void firstPart() {
        int codedChars = 0;
        int inMemoryChars = 0;
        for (String line : readInput()) {
            codedChars += line.length();
            inMemoryChars += countInMemoryChars(line);
        }
        System.out.println((codedChars - inMemoryChars));
    }

    private static void secondPart() {
        int codedChars = 0;
        int encodedChars = 0;
        for (String line : readInput()) {
            codedChars += line.length();
            encodedChars += countEncodedString(line);
        }
        System.out.println((encodedChars - codedChars));
    }

    private static int countInMemoryChars(String line) {
        int codeCharCount = line.length();
        codeCharCount -= 2;
        String stripped = line.substring(1, line.length() - 1);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < stripped.length()) {
            if (stripped.charAt(i) == '\\') {
                if (stripped.charAt(i + 1) == '\\') {
                    --codeCharCount;
                    sb.append('\\');
                    i += 2;
                } else if (stripped.charAt(i + 1) == '"') {
                    --codeCharCount;
                    sb.append('"');
                    i += 2;
                } else if (stripped.charAt(i + 1) == 'x') {
                    String coded = Character.toString(Integer.parseInt(stripped.substring(i + 2, i + 4), 16));
                    codeCharCount -= 3;
                    sb.append(coded);
                    i += 4;
                } else {
                    throw new IllegalArgumentException(
                            "can not work with char: '" + stripped.charAt(i + 1) + "' ins tring: '" + line + "'");
                }
            } else {
                sb.append(stripped.charAt(i));
                ++i;
            }
        }
        assert (sb.length() == codeCharCount);
        return codeCharCount;
    }

    private static int countEncodedString(String line) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (int i = 0; i < line.length(); ++i) {
            if (TO_ESCAPE.contains(line.charAt(i))) {
                sb.append("\\" + line.charAt(i));
            } else {
                sb.append(line.charAt(i));
            }
        }
        sb.append("\"");
        return sb.length();
    }

    private static final List<String> readInput() {
        try (var reader = new BufferedReader(
                new InputStreamReader(Matchsticks.class.getResourceAsStream("/day08.txt")))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            return INPUT.lines().collect(Collectors.toList());
        }
    }
    
    private static final String INPUT = "\"sjdivfriyaaqa\\xd2v\\\"k\\\"mpcu\\\"yyu\\\"en\"\n"
            + "\"vcqc\"\n"
            + "\"zbcwgmbpijcxu\\\"yins\\\"sfxn\"\n"
            + "\"yumngprx\"\n"
            + "\"bbdj\"\n"
            + "\"czbggabkzo\\\"wsnw\\\"voklp\\\"s\"\n"
            + "\"acwt\"\n"
            + "\"aqttwnsohbzian\\\"evtllfxwkog\\\"cunzw\"\n"
            + "\"ugvsgfv\"\n"
            + "\"xlnillibxg\"\n"
            + "\"kexh\\\"pmi\"\n"
            + "\"syvugow\"\n"
            + "\"m\\\"ktqnw\"\n"
            + "\"yrbajyndte\\\\rm\"\n"
            + "\"f\\\"kak\\x70sn\\xc4kjri\"\n"
            + "\"yxthr\"\n"
            + "\"alvumfsjni\\\"kohg\"\n"
            + "\"trajs\\x5brom\\xf1yoijaumkem\\\"\\\"tahlzs\"\n"
            + "\"\\\"oedr\\\"pwdbnnrc\"\n"
            + "\"qsmzhnx\\\"\"\n"
            + "\"\\\"msoytqimx\\\\tbklqz\"\n"
            + "\"mjdfcgwdshrehgs\"\n"
            + "\"\\\"rivyxahf\\\"\"\n"
            + "\"ciagc\\x04bp\"\n"
            + "\"xkfc\"\n"
            + "\"xrgcripdu\\x4c\\xc4gszjhrvumvz\\\"mngbirb\"\n"
            + "\"gvmae\\\"yiiujoqvr\\\"mkxmgbbut\\\"u\"\n"
            + "\"ih\"\n"
            + "\"ncrqlejehs\"\n"
            + "\"mkno\\x43pcfdukmemycp\"\n"
            + "\"uanzoqxkpsksbvdnkji\\\"feamp\"\n"
            + "\"axoufpnbx\\\\ao\\x61pfj\\\"b\"\n"
            + "\"dz\\\\ztawzdjy\"\n"
            + "\"ihne\\\"enumvswypgf\"\n"
            + "\"\\\"dgazthrphbshdo\\\\vuqoiy\\\"\"\n"
            + "\"dlnmptzt\\\\zahwpylc\\\\b\\\"gmslrqysk\"\n"
            + "\"mhxznyzcp\"\n"
            + "\"rebr\\\"amvxw\\x5fmbnfpkkeghlntavj\"\n"
            + "\"lades\\x47ncgdof\\\"\\\"jmbbk\"\n"
            + "\"dwxuis\\xa5wdkx\\\\z\\\"admgnoddpgkt\\\\zs\"\n"
            + "\"g\\\\k\\x27qsl\\x34hwfglcdxqbeclt\\xca\\\\\"\n"
            + "\"lhyjky\\\\m\\\"pvnm\\\\xmynpxnlhndmahjl\"\n"
            + "\"c\\\"uxabbgorrpprw\\\"xas\\\\vefkxioqpt\"\n"
            + "\"rfrvjxpevcmma\\x71gtfipo\"\n"
            + "\"fgh\\\"kcwoqwfnjgdlzfclprg\\\"q\"\n"
            + "\"onxnwykrba\"\n"
            + "\"hkkg\\x60f\\\"tjzsanpvarzgkfipl\"\n"
            + "\"\\\"aintes\\\"ofq\\\"juiaqlqxmvpe\\\\a\"\n"
            + "\"wiyczzs\\\"ciwk\"\n"
            + "\"mfqeu\"\n"
            + "\"v\\xe1z\\x7ftzalmvdmncfivrax\\\\rjwq\"\n"
            + "\"k\\\"vtg\"\n"
            + "\"exhrtdugeml\\xf0\"\n"
            + "\"behnchkpld\"\n"
            + "\"mhgxy\\\"mfcrg\\xc5gnp\\\"\\\"osqhj\"\n"
            + "\"rlvjy\"\n"
            + "\"awe\"\n"
            + "\"ctwy\"\n"
            + "\"vt\"\n"
            + "\"\\x54t\"\n"
            + "\"zugfmmfomz\"\n"
            + "\"cv\\\"cvcvfaada\\x04fsuqjinbfh\\xa9cq\\xd2c\\\"d\"\n"
            + "\"oj\"\n"
            + "\"xazanf\\\"wbmcrn\"\n"
            + "\"\\\\\\\\zkisyjpbzandqikqjqvee\"\n"
            + "\"dpsnbzdwnxk\\\\v\"\n"
            + "\"sj\\\"tuupr\\\\oyoh\"\n"
            + "\"myvkgnw\\x81q\\xaaokt\\\\emgejbsyvxcl\\\\\\xee\"\n"
            + "\"ejeuqvunjcirdkkpt\\\"nlns\"\n"
            + "\"twmlvwxyvfyqqzu\"\n"
            + "\"\\\"xwtzdp\\x98qkcis\\\"dm\\\\\\\"ep\\\"xyykq\"\n"
            + "\"vvcq\\\\expok\"\n"
            + "\"wgukjfanjgpdjb\"\n"
            + "\"\\\"mjcjajnxy\\\\dcpc\"\n"
            + "\"wdvgnecw\\\\ab\\x44klceduzgsvu\"\n"
            + "\"dqtqkukr\\\"iacngufbqkdpxlwjjt\"\n"
            + "\"\\\"xj\\\"\\x66qofsqzkoah\"\n"
            + "\"nptiwwsqdep\"\n"
            + "\"gsnlxql\\x30mjl\"\n"
            + "\"yeezwokjwrhelny\\\"\"\n"
            + "\"bjauamn\\\\izpmzqqasid\"\n"
            + "\"tvjdbkn\\\"tiziw\\x82r\"\n"
            + "\"w\"\n"
            + "\"xwoakbbnjnypnaa\\xa9wft\\\"slrmoqkl\"\n"
            + "\"vwxtnlvaaasyruykgygrvpiopzygf\\\"vq\"\n"
            + "\"qdancvnvmhlmpj\\\\isdxs\"\n"
            + "\"xzc\\\\elw\"\n"
            + "\"b\\\"wxeqvy\\\"qf\\\"g\\xcaoklsucwicyw\\\"dovr\"\n"
            + "\"yomlvvjdbngz\\\"rly\\\"afr\"\n"
            + "\"bfb\\\"x\\\"aweuwbwmoa\\x13\\\"t\\\"zhr\"\n"
            + "\"\\\"dmfoxb\\\"qvpjzzhykt\\xd2\\\"\\\"ryhxi\"\n"
            + "\"psqef\\\"yu\\\\qiflie\\\"\\x79w\"\n"
            + "\"arzewkej\\\"lqmh\\\\sayyusxxo\\\\\"\n"
            + "\"vuvvp\"\n"
            + "\"hc\\\"lg\\x6bcpupsewzklai\\\"l\"\n"
            + "\"cjdfygc\\\"auorqybnuqghsh\\x10\"\n"
            + "\"j\"\n"
            + "\"wqjexk\\\"eyq\\\\lbroqhk\\\\dqzsqk\"\n"
            + "\"dws\\\"ru\\\"dvxfiwapif\\\"oqwzmle\"\n"
            + "\"agcykg\\\\jt\\\\vzklqjvknoe\"\n"
            + "\"kksd\\\"jmslja\\\\z\\\"y\\\\b\\xaagpyojct\"\n"
            + "\"nnpipxufvbfpoz\\\"jno\"\n"
            + "\"dtw\"\n"
            + "\"xlolvtahvgqkx\\\\dgnhj\\\\spsclpcxv\\\\\"\n"
            + "\"mxea\\\\mbjpi\"\n"
            + "\"lgbotkk\\\"zmxh\\\\\\\\qji\\\"jszulnjsxkqf\"\n"
            + "\"lwckmhwhx\\\"gmftlb\\x91am\"\n"
            + "\"xxdxqyxth\"\n"
            + "\"\\\"lmqhwkjxmvayxy\"\n"
            + "\"tf\"\n"
            + "\"qy\"\n"
            + "\"wdqmwxdztax\\\"m\\\"\\x09\\x11xdxmfwxmtqgwvf\"\n"
            + "\"\\xcbnazlf\\\"ghziknszmsrahaf\"\n"
            + "\"e\\x6aupmzhxlvwympgjjpdvo\\\"kylfa\"\n"
            + "\"\\x81vhtlillb\\xactgoatva\"\n"
            + "\"dvnlgr\"\n"
            + "\"f\"\n"
            + "\"xg\\xfacwizsadgeclm\"\n"
            + "\"vnnrzbtw\\\"\\\\prod\\\\djbyppngwayy\\\"\"\n"
            + "\"lrt\\xf4jahwvfz\"\n"
            + "\"aqpnjtom\\\"ymkak\\\\dadfybqrso\\\\fwv\"\n"
            + "\"gz\\\"aac\\\"mrbk\\\"ktommrojraqh\"\n"
            + "\"wycamwoecsftepfnlcdkm\"\n"
            + "\"nrhddblbuzlqsl\\x9cben\"\n"
            + "\"vckxhyqkmqmdseazcykrbysm\"\n"
            + "\"sil\\xbbtevmt\\\"gvrvybui\\\"faw\\\"j\"\n"
            + "\"cjex\\\\tp\\x45pzf\"\n"
            + "\"asjobvtxszfodgf\\\"ibftg\"\n"
            + "\"gkyjyjdrxdcllnh\\\"sjcibenrdnxv\"\n"
            + "\"oswsdpjyxpbwnqbcpl\\\"yrdvs\\\\zq\"\n"
            + "\"\\\"\\\"tyowzc\\\\fycbp\\\"jbwrbvgui\"\n"
            + "\"cbpcabqkdgzmpgcwjtrchxp\"\n"
            + "\"iyrzfh\\x45gw\\\"fdlfpiaap\\x31xqq\"\n"
            + "\"evgksznidz\"\n"
            + "\"b\\\\w\\\\\"\n"
            + "\"loufizbiy\\x57aim\\\"bgk\"\n"
            + "\"qjfyk\"\n"
            + "\"g\\\"anmloghvgr\\x07zwqougqhdz\"\n"
            + "\"usbbmwcxd\\\\bdgg\"\n"
            + "\"htitqcpczml\"\n"
            + "\"eke\\\\cqvpexqqk\\\"to\\\"tqmljrpn\\xe6lji\\\"\"\n"
            + "\"g\\xd2ifdsej\"\n"
            + "\"h\\\"sk\\\"haajajpagtcqnzrfqn\\xe6btzo\"\n"
            + "\"wfkuffdxlvm\\\\cvlyzlbyunclhmpp\"\n"
            + "\"myaavh\\\"spue\"\n"
            + "\"hqvez\\x68d\\\"eo\\\"eaioh\"\n"
            + "\"s\\\"qd\\\"oyxxcglcdnuhk\"\n"
            + "\"ilqvar\"\n"
            + "\"srh\"\n"
            + "\"puuifxrfmpc\\\"bvalwi\\x2blu\\\\\"\n"
            + "\"yywlbutufzysbncw\\\\nqsfbhpz\\\"mngjq\"\n"
            + "\"zbl\\\\jfcuop\"\n"
            + "\"hjdouiragzvxsqkreup\\\\\"\n"
            + "\"qi\"\n"
            + "\"ckx\\\\funlj\\xa7ahi\"\n"
            + "\"k\"\n"
            + "\"ufrcnh\\\"ajteit\"\n"
            + "\"cqv\\\"bgjozjj\\x60x\\xa8yhvmdvutchjotyuz\"\n"
            + "\"hkuiet\\\"oku\\x8cfhumfpasl\"\n"
            + "\"\\\"\\\\sbe\\x4d\"\n"
            + "\"vhknazqt\"\n"
            + "\"eyyizvzcahgflvmoowvs\\\\jhvygci\"\n"
            + "\"kki\\x3ewcefkgtjap\\\"xtpxh\\\"lzepoqj\"\n"
            + "\"wvtk\"\n"
            + "\"\\\"ynet\"\n"
            + "\"zh\\\\obk\\\"otagx\\x59txfzf\"\n"
            + "\"ocowhxlx\\xe6zqg\\x63wx\\\\tclkhq\\\\vmaze\"\n"
            + "\"w\\\"cf\"\n"
            + "\"qpniprnrzrnvykghqnalr\"\n"
            + "\"jctcqra\\\"\\x05dhlydpqamorqjsijt\\\\xjdgt\"\n"
            + "\"sig\"\n"
            + "\"qhlbidbflwxe\\\"xljbwls\\x20vht\"\n"
            + "\"irmrebfla\\xefsg\\\"j\"\n"
            + "\"nep\"\n"
            + "\"hjuvsqlizeqobepf\"\n"
            + "\"guzbcdp\\\"obyh\"\n"
            + "\"\\\"mjagins\\xf9tqykaxy\\\"\"\n"
            + "\"knvsdnmtr\\\"zervsb\"\n"
            + "\"hzuy\"\n"
            + "\"zza\\\"k\\\"buapb\\\\elm\\xfeya\"\n"
            + "\"lrqar\\\"dfqwkaaqifig\\\"uixjsz\"\n"
            + "\"\\\"azuo\\x40rmnlhhluwsbbdb\\x32pk\\\\yu\\\"pbcf\"\n"
            + "\"dplkdyty\"\n"
            + "\"rfoyciebwlwphcycmguc\"\n"
            + "\"ivnmmiemhgytmlprq\\\\eh\"\n"
            + "\"lhkyzaaothfdhmbpsqd\\\\yyw\"\n"
            + "\"tnlzifupcjcaj\"\n"
            + "\"\\\\qiyirsdrfpmu\\\\\\x15xusifaag\"\n"
            + "\"\\\\lcomf\\\\s\"\n"
            + "\"uramjivcirjhqcqcg\"\n"
            + "\"kkbaklbxfxikffnuhtu\\xc6t\\\"d\"\n"
            + "\"n\\xefai\"\n"
            + "\"\\\"toy\\\"bnbpevuzoc\\\"muywq\\\"gz\\\"grbm\"\n"
            + "\"\\\"muu\\\\wt\"\n"
            + "\"\\\\srby\\\"ee\"\n"
            + "\"erf\\\"gvw\\\"swfppf\"\n"
            + "\"pbqcgtn\\\"iuianhcdazfvmidn\\\\nslhxdf\"\n"
            + "\"uxbp\"\n"
            + "\"up\\\\mgrcyaegiwmjufn\"\n"
            + "\"nulscgcewj\\\\dvoyvhetdegzhs\\\"\"\n"
            + "\"masv\\\"k\\\\rzrb\"\n"
            + "\"qtx\\x79d\\\"xdxmbxrvhj\"\n"
            + "\"fid\\\\otpkgjlh\\\"qgsvexrckqtn\\xf4\"\n"
            + "\"tagzu\"\n"
            + "\"bvl\\\\\\\"noseec\"\n"
            + "\"\\\\xgicuuh\"\n"
            + "\"w\\\"a\\\"npemf\"\n"
            + "\"sxp\"\n"
            + "\"nsmpktic\\x8awxftscdcvijjobnq\\\"gjd\"\n"
            + "\"uks\\\"\\\"jxvyvfezz\\\"aynxoev\\\"cuoav\"\n"
            + "\"m\"\n"
            + "\"lkvokj\"\n"
            + "\"vkfam\\\"yllr\\\"q\\x92o\\x4ebecnvhshhqe\\\\\"\n"
            + "\"efdxcjkjverw\"\n"
            + "\"lmqzadwhfdgmep\\x02tzfcbgrbfekhat\"\n"
            + "\"cpbk\\x9azqegbpluczssouop\\x36ztpuoxsw\"\n"
            + "\"cqwoczxdd\\\"erdjka\"\n"
            + "\"cwvqnjgbw\\\\fxdlby\"\n"
            + "\"mvtm\"\n"
            + "\"lt\\\"bbqzpumplkg\"\n"
            + "\"ntd\\xeeuwweucnuuslqfzfq\"\n"
            + "\"y\\xabl\\\"dbebxjrlbmuoo\\\\\\x1au\"\n"
            + "\"qjoqx\\\\a\"\n"
            + "\"pu\\\"ekdnfpmly\\xbago\\\"\"\n"
            + "\"fjhhdy\"\n"
            + "\"arl\"\n"
            + "\"xcywisim\\\"bwuwf\\\"\\\"raepeawwjub\"\n"
            + "\"pbe\"\n"
            + "\"dbnqfpzyaumxtqnd\\xc5dcqrkwyop\"\n"
            + "\"ojv\\x40vtkwgkqepm\\x8bzft\\\\vedrry\"\n"
            + "\"wggqkfbwqumsgajqwphjec\\\"mstxpwz\"\n"
            + "\"zjkbem\"\n"
            + "\"icpfqxbelxazlls\"\n"
            + "\"pvpqs\\\\abcmtyielugfgcv\\\"tjxapxqxnx\"\n"
            + "\"oqddwlvmtv\\\"\\x39lyybylfb\\\"jmngnpjrdw\"\n"
            + "\"gisgbve\"\n"
            + "\"\\\"aglg\"\n"
            + "\"y\\\"\\\"ss\\xafvhxlrjv\"\n"
            + "\"qbgqjsra\"\n"
            + "\"ihshbjgqpdcljpmdwdprwloy\"\n"
            + "\"djja\\\\wcdn\\\"svkrgpqn\\\"uz\\\"hc\\x43hj\"\n"
            + "\"cbjm\"\n"
            + "\"pnn\"\n"
            + "\"pqvh\\\"noh\"\n"
            + "\"\\\"\\\\fdktlp\"\n"
            + "\"ncea\"\n"
            + "\"pqgzphiyy\"\n"
            + "\"\\xbedovhxuipaohlcvkwtxwmpz\\\"ckaif\\\"r\"\n"
            + "\"arjuzbjowqciunfwgxtph\\\"vlhy\\\"n\"\n"
            + "\"c\"\n"
            + "\"nrpdxunulgudqzlhtae\"\n"
            + "\"iefheu\\\"uru\\\"\"\n"
            + "\"aqijysxuijud\\\"np\\\\opbichhudil\\xbesum\"\n"
            + "\"pfpevmtstl\\\"lde\\\"bzr\\\"vspdxs\"\n"
            + "\"vparfbdjwvzsocpnzhp\"\n"
            + "\"g\\x4ffxaarafrsjthq\\\\\\xc1rw\"\n"
            + "\"ng\\\\rqx\\\\gwpzucbh\\xafl\"\n"
            + "\"rw\\\"nf\\\\dna\"\n"
            + "\"jkkeahxurxla\\\\g\\xb3czrlsyimmwcwthr\"\n"
            + "\"twaailoypu\\\"oas\\\"kpuuyedlaw\\\\\\xb0vzt\"\n"
            + "\"hznex\\\\gdiqvtugi\"\n"
            + "\"imdibsunjeswhk\"\n"
            + "\"ta\\\\icileuzpxro\\\"cfmv\\\"mzp\"\n"
            + "\"coykr\\x57luiysucfaflmilhlehmvzeiepo\"\n"
            + "\"u\\x3dfh\\xd4yt\"\n"
            + "\"piw\\x1bz\\\"eowy\\\"vfk\\\"wqiekw\"\n"
            + "\"gan\\\"y\"\n"
            + "\"p\\\"bevidoazcznr\\\"hddxuuq\\\"\"\n"
            + "\"bwzucczznutbxe\"\n"
            + "\"z\\\"viqgyqjisior\\\\iecosmjbknol\"\n"
            + "\"dmlpcglcfkfsctxydjvayhymv\\x3c\\\\gp\"\n"
            + "\"bfvkqrintbbvgfv\"\n"
            + "\"xlzntrgdck\\\"cprc\\xadczyarbznqmuhxyuh\"\n"
            + "\"uqdxnuwioc\\\"kdytxq\\\\ig\"\n"
            + "\"xrafmucpmfi\"\n"
            + "\"vr\\\"hltmfrge\"\n"
            + "\"eonf\\\"nt\\\\wtcnsocs\"\n"
            + "\"j\\xb7xoslyjeyjksplkqixncgkylkw\"\n"
            + "\"njw\\\"pefgfbez\\x9axshdmplxzquqe\"\n"
            + "\"di\\x58bvptfsafirpc\"\n"
            + "\"l\\x1fkco\"\n"
            + "\"x\"\n"
            + "\"mprndo\\\"n\"\n"
            + "\"psegit\"\n"
            + "\"svbdnkkuuqs\\\"sqxu\\\"oqcyz\\\"aizashk\"\n"
            + "\"cwkljukxer\\\\\\\"\\\\nff\\\"esjwiyaoy\"\n"
            + "\"ilxrkgbjjxpvhdtq\\\"cpiuoofdnkpp\"\n"
            + "\"hlngi\\\"ulxep\\\\qohtmqnqjb\\\"rkgerho\"\n"
            + "\"gxws\\\"bcgm\\\"p\"\n"
            + "\"bv\\\"mds\\\\zhfusiepgrz\\\\b\\x32fscdzz\"\n"
            + "\"l\\xfampwtme\\x69qvxnx\\\"\\\"\\xc4jruuymjxrpsv\"\n"
            + "\"qqmxhrn\"\n"
            + "\"xziq\\\\\\x18ybyv\\x9am\\\"neacoqjzytertisysza\"\n"
            + "\"aqcbvlvcrzceeyx\\\\j\\\"\\\"x\"\n"
            + "\"yjuhhb\"\n"
            + "\"\\x5em\\\"squulpy\"\n"
            + "\"dpbntplgmwb\"\n"
            + "\"utsgfkm\\\\vbftjknlktpthoeo\"\n"
            + "\"ccxjgiocmuhf\\\"ycnh\"\n"
            + "\"lltj\\\"kbbxi\"";
}
