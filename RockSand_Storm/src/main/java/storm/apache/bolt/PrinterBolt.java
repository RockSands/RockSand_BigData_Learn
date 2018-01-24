package storm.apache.bolt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.google.gson.Gson;

import storm.apache.bean.OggRecord;

public class PrinterBolt implements IRichBolt {

	private static final long serialVersionUID = 1L;

	OutputCollector collector = null;
	Gson gson = null;

	@SuppressWarnings("rawtypes")
	@Override
	// prepare仅执行一次,相当于初始化
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		gson = new Gson();
	}

	@Override
	public void execute(Tuple input) {
		List<OggRecord> insertOggRecords = new LinkedList<OggRecord>();
		List<OggRecord> updatetOggRecords = new LinkedList<OggRecord>();
		List<OggRecord> deleteOggRecords = new LinkedList<OggRecord>();
		OggRecord index = null;
		for (int i = 0; i < input.getValues().size(); i++) {
			System.out.println("=PrinterBolt=>" + input.getString(i));
			index = gson.fromJson(input.getString(i), OggRecord.class);
			if ("I".equals(index.getOp_type())) {
				insertOggRecords.add(index);
			} else if ("U".equals(index.getOp_type())) {
				updatetOggRecords.add(index);
			} else if ("D".equals(index.getOp_type())) {
				deleteOggRecords.add(index);
			} else {
				// 抛弃
			}
		}
		if (insertOggRecords.size() > 0) {
			collector.emit("insert", new Values(insertOggRecords));
		}
		if (updatetOggRecords.size() > 0) {
			collector.emit("update", new Values(updatetOggRecords));
		}
		if (deleteOggRecords.size() > 0) {
			collector.emit("delete", new Values(deleteOggRecords));
		}
		// 该处进行会写,才会记录Kafka的offset
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	// 定义输出Fields
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("insert", new Fields("insert"));
		declarer.declareStream("update", new Fields("update"));
		declarer.declareStream("delete", new Fields("delete"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
