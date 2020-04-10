package com.spark.szhb_master.entity;

import java.io.Serializable;
import java.util.List;

public class CommonSymbol {

    /**
     * ch : market.overview
     * data : [{"Open":7328.78,"Close":7232.91,"Low":7069.01,"High":7398,"Amount":68038.50660895434,"Count":112588,"Vol":4974520,"Symbol":"BTC","Type":60,"Scale":-1.31,"Convert":7232.91},{"Open":7327.07,"Close":7224.85,"Low":7062.31,"High":7393.33,"Amount":15728.054301528817,"Count":34114,"Vol":1147530,"Symbol":"BTC","Type":120,"Scale":-1.4,"Convert":7224.85},{"Open":7391.68,"Close":7277.02,"Low":7110,"High":7463.51,"Amount":501346.16073379555,"Count":818567,"Vol":36933412,"Symbol":"BTC","Type":200,"Scale":-1.55,"Convert":7277.02},{"Open":169.31,"Close":167.065,"Low":161.633,"High":173.619,"Amount":1849964.0946375816,"Count":120000,"Vol":31419322,"Symbol":"ETH","Type":60,"Scale":-1.33,"Convert":167.065},{"Open":169.399,"Close":167.174,"Low":161.858,"High":173.687,"Amount":326044.26067523565,"Count":49180,"Vol":5544340,"Symbol":"ETH","Type":120,"Scale":-1.31,"Convert":167.174},{"Open":170.815,"Close":168.316,"Low":163,"High":175.202,"Amount":9434858.204857547,"Count":807273,"Vol":161992126,"Symbol":"ETH","Type":200,"Scale":-1.46,"Convert":168.316},{"Open":2.724,"Close":2.635,"Low":2.549,"High":2.763,"Amount":5.107492702380863E7,"Count":59288,"Vol":13829516,"Symbol":"EOS","Type":60,"Scale":-3.27,"Convert":2.635},{"Open":2.731,"Close":2.638,"Low":2.548,"High":2.768,"Amount":9046463.893223764,"Count":16063,"Vol":2442934,"Symbol":"EOS","Type":120,"Scale":-3.41,"Convert":2.638},{"Open":2.754,"Close":2.649,"Low":2.565,"High":2.792,"Amount":1.913975515684878E8,"Count":305052,"Vol":52262050,"Symbol":"EOS","Type":200,"Scale":-3.81,"Convert":2.649},{"Open":46.593,"Close":45.412,"Low":43.569,"High":47.002,"Amount":519437.2759947799,"Count":24550,"Vol":2386144,"Symbol":"LTC","Type":60,"Scale":-2.53,"Convert":45.412},{"Open":46.617,"Close":45.443,"Low":43.561,"High":47.008,"Amount":167528.160196234,"Count":6811,"Vol":770956,"Symbol":"LTC","Type":120,"Scale":-2.52,"Convert":45.443},{"Open":46.791,"Close":45.528,"Low":43.652,"High":47.241,"Amount":3759658.9190413756,"Count":137560,"Vol":17311216,"Symbol":"LTC","Type":200,"Scale":-2.7,"Convert":45.528},{"Open":257.755,"Close":256.238,"Low":246.01,"High":261.298,"Amount":130035.7144990394,"Count":28455,"Vol":3333588,"Symbol":"BCH","Type":60,"Scale":-0.59,"Convert":256.238},{"Open":258.201,"Close":256.693,"Low":246.606,"High":261.442,"Amount":39092.60788600107,"Count":9142,"Vol":1005914,"Symbol":"BCH","Type":120,"Scale":-0.58,"Convert":256.693},{"Open":260.007,"Close":258.499,"Low":248.12,"High":263.802,"Amount":1331789.5946732296,"Count":261611,"Vol":34505854,"Symbol":"BCH","Type":200,"Scale":-0.58,"Convert":258.499},{"Open":0.2006,"Close":0.1955,"Low":0.1892,"High":0.2028,"Amount":7.017357708965193E7,"Count":8951,"Vol":1388904,"Symbol":"XRP","Type":60,"Scale":-2.54,"Convert":0.1955},{"Open":0.2009,"Close":0.1958,"Low":0.1895,"High":0.203,"Amount":2.3513493085974023E7,"Count":3494,"Vol":466342,"Symbol":"XRP","Type":120,"Scale":-2.54,"Convert":0.1958},{"Open":0.201,"Close":0.1958,"Low":0.1896,"High":0.2038,"Amount":3.84249409503388E8,"Count":59032,"Vol":7664350,"Symbol":"XRP","Type":200,"Scale":-2.59,"Convert":0.1958},{"Open":5.636,"Close":5.496,"Low":5.365,"High":5.724,"Amount":3093464.2168124393,"Count":8463,"Vol":1737016,"Symbol":"ETC","Type":60,"Scale":-2.48,"Convert":5.496},{"Open":5.626,"Close":5.499,"Low":5.362,"High":5.712,"Amount":852487.4606929864,"Count":3550,"Vol":476468,"Symbol":"ETC","Type":120,"Scale":-2.26,"Convert":5.499},{"Open":5.608,"Close":5.471,"Low":5.333,"High":5.708,"Amount":1.0031218613703001E7,"Count":53460,"Vol":5585914,"Symbol":"ETC","Type":200,"Scale":-2.44,"Convert":5.471},{"Open":0.01379,"Close":0.01348,"Low":0.01305,"High":0.01397,"Amount":3.7508130223807544E8,"Count":2727,"Vol":510712,"Symbol":"TRX","Type":60,"Scale":-2.25,"Convert":0.01348},{"Open":0.0139,"Close":0.01347,"Low":0.01305,"High":0.01396,"Amount":4.6743131835903384E7,"Count":708,"Vol":63522,"Symbol":"TRX","Type":120,"Scale":-3.09,"Convert":0.01347},{"Open":0.01381,"Close":0.01343,"Low":0.01306,"High":0.014,"Amount":1.0494043493954526E9,"Count":17657,"Vol":1432536,"Symbol":"TRX","Type":200,"Scale":-2.75,"Convert":0.01343},{"Open":189.326,"Close":192.271,"Low":182.656,"High":192.271,"Amount":275577.96528300014,"Count":19970,"Vol":5215972,"Symbol":"BSV","Type":60,"Scale":1.56,"Convert":192.271},{"Open":189.819,"Close":192.765,"Low":183.217,"High":192.765,"Amount":45678.07745790607,"Count":6608,"Vol":865628,"Symbol":"BSV","Type":120,"Scale":1.55,"Convert":192.765},{"Open":190.106,"Close":193.481,"Low":183.51,"High":193.825,"Amount":1343844.30226801,"Count":156652,"Vol":25640560,"Symbol":"BSV","Type":200,"Scale":1.78,"Convert":193.481}]
     */

    private String ch;
    private List<NewCurrency> data;

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public List<NewCurrency> getData() {
        return data;
    }

    public void setData(List<NewCurrency> data) {
        this.data = data;
    }
}
