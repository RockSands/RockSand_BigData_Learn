package storm.apache.bolt;

import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import storm.apache.bean.OggRecord;

public class InsertBolt implements IRichBolt {

	private static final long serialVersionUID = 1L;
	OutputCollector collector = null;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Tuple input) {
		OggRecord oggRecord = null;
		int offset = 0;
		if (List.class.isInstance(input.getValue(0))) {
			for (Object o : (List) input.getValue(0)) {
				if (OggRecord.class.isInstance(o)) {
					oggRecord = (OggRecord) o;
					System.out.println(
							"=插入=>{" + oggRecord.getAfter().getName().trim() + " , " + oggRecord.getAfter().getValue());
					offset = offset + oggRecord.getAfter().getValue();
				}
			}
		}
		if (offset != 0) {
			collector.emit(new Values(offset));
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("offset"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
