package am.parsing;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import am.classes.Result;
import am.structures.ResultQueue;

/**
 * NmapParser is used for parsing XML output of a nmap command,
 * creating a <code>Result</code> object and push it to the <code>ResultQueue</code>.
 * 
 * @author C. Patsouras I. Venieris
 * @version 2
 */
public class NmapParser {

	/**
	 * 	@param xmls		XML output of a nmap command.
	 * 	@param hashkey	hashkey of the Agent that executed this command.
	 * 	@param id		executed command's job identifier.
	 * 	
	 * 	@return true if parsing was successful and no exception occurred
	 * 			false if not so
	 */
	public static boolean parse(String xmls, int hashkey, int id) {
		try {
			// if an unfinished job is passed for some reason
			// avoid parsing this result and pushing it to the queue.
			if (xmls.contains("</nmaprun>") == false) return false;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmls));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			String executed = doc.getDocumentElement().getAttribute("args");
			// creating a node list or the given XML
			NodeList nodeLst = doc.getDocumentElement().getChildNodes();
			Node node2 = null ;
			NodeList nodel2 = null ;			
			Nmaprun result = new Nmaprun(); // decribes result of XML
			Hosts hosts = new Hosts();
			result.setHosts(hosts);
			// checking node name and stores info
			// uses "getAttribute" to read a specific label
			// if this label isn't found in XML, an empty string is returned
			for (int i = 0 ; i < nodeLst.getLength() ; i++) {
				Node node = nodeLst.item(i);
				String ndName = node.getNodeName();
				Element elem = (Element) node;
				if(ndName == "scaninfo") {
					// parsing scaninfo section
					ScanInfo sInfo = new ScanInfo();
					sInfo.setType(elem.getAttribute("type"));
					sInfo.setProtocol(elem.getAttribute("protocol"));
					sInfo.setNumservices(elem.getAttribute("numservices"));
					sInfo.setServices(elem.getAttribute("services"));
					result.setScaninfo(sInfo);
				}
				else if(ndName == "verbose") {
					// parsing verbose section
					Verbose verb = new Verbose();
					verb.setLevel(elem.getAttribute("level"));
					result.setVerbose(verb);
				}
				else if (ndName == "debugging") {
					// parsing debugging section
					Debugging dbg = new Debugging();
					dbg.setLevel(elem.getAttribute("level"));
					result.setDebbuging(dbg);
				}
				else if (ndName == "host") {
					// parsing host section, creating objects for host's child nodes
					Host host = new Host();
					hosts.push(host);
					host.setStime(elem.getAttribute("starttime"));
					host.setEtime(elem.getAttribute("endtime"));
					HostStatus hSt = new HostStatus();
					host.setStatus(hSt);
					HostUptime hUpt = new HostUptime();
					host.setUptime(hUpt);
					TcpSequence hTcpS = new TcpSequence();
					host.setTcpsequence(hTcpS);
					IpIdSequence hIpId = new IpIdSequence();
					host.setIpidsequence(hIpId);
					TcpTsSequence hTcpTsS = new TcpTsSequence();
					host.setTcptssequence(hTcpTsS);
					Distance hDis = new Distance();
					host.setDistance(hDis);
					Os hOs = new Os();
					host.setOs(hOs);
					Ports ports = new Ports();
					host.setPorts(ports);
					Hostnames hNames = new Hostnames();
					host.setHostnames(hNames);
					Trace trace = new Trace();
					host.setTrace(trace);
					if (node.hasChildNodes()){
						// scanning host's child nodes
						NodeList nodel = node.getChildNodes();
						for (int j = 0 ; j < nodel.getLength() ; j ++) {
							node2 = nodel.item(j);
							elem = (Element) node2;
							ndName = node2.getNodeName();
							if(ndName == "status") {
								// parsing host's status section
								hSt.setState(elem.getAttribute("state"));
								hSt.setReason(elem.getAttribute("reason"));
								hSt.setReasonTtl(elem.getAttribute("reason_ttl"));
							}
							else if (ndName == "address") {
								// parsing host's address section
								HostAddress hAddr = new HostAddress();
								hAddr.setAddr(elem.getAttribute("addr"));
								hAddr.setAddrType(elem.getAttribute("addrtype"));								
								hAddr.setVendor(elem.getAttribute("vendor"));
								host.push(hAddr);
							}
							else if(ndName == "uptime") {
								// parsing host's uptime section
								hUpt.setSeconds(elem.getAttribute("seconds"));
								hUpt.setLastboot(elem.getAttribute("lastboot"));
							}
							else if(ndName == "tcpsequence") {
								// parsing host's tcpsequence section
								hTcpS.setIndex(elem.getAttribute("index"));
								hTcpS.setDifficulty(elem.getAttribute("difficulty"));
								hTcpS.setValues(elem.getAttribute("values"));
							}
							else if(ndName == "ipidsequence") {
								// parsing host's ipidsequence section
								hIpId.setKlass(elem.getAttribute("class"));
								hIpId.setValues(elem.getAttribute("values"));
							}
							else if(ndName == "tcptssequence") {
								// parsing host's tcptssequence section
								hTcpTsS.setKlass(elem.getAttribute("class"));
								hTcpTsS.setValues(elem.getAttribute("values"));
							}
							else if(ndName == "distance") {
								// parsing host's distance section
								hDis.setValue(elem.getAttribute("value"));
							}
							else if (ndName == "os") {
								// creating a nodelist for host's OS info
								if (node2.hasChildNodes()){
									nodel2 = node2.getChildNodes();
									// scanning host's OS child nodes
									for (int k = 0 ; k < nodel2.getLength(); k++) {
										node2 = nodel2.item(k);
										ndName = node2.getNodeName();
										elem = (Element) node2 ;
										if (ndName == "osmatch") {
											// parsing an OSmatch node
											OsMatch osM = new OsMatch();
											osM.setName(elem.getAttribute("name"));
											osM.setAccuracy(elem.getAttribute("accuracy"));
											osM.setLine(elem.getAttribute("line"));
											// creating child node list for OSmatch
											if (node2.hasChildNodes()){
												NodeList nodel3 = node2.getChildNodes();
												for (int z = 0 ; z < nodel3.getLength() ; z++){
													Node node3 = nodel3.item(z);
													elem = (Element) node3;
													ndName = node3.getNodeName();
													if (ndName == "osclass") {
														// parsing OSclass
														OsClass osMC = new OsClass();
														osMC.setType(elem.getAttribute("type"));
														osMC.setVendor(elem.getAttribute("vendor"));
														osMC.setOsFamily(elem.getAttribute("osfamily"));
														osMC.setOsGen(elem.getAttribute("osgen"));
														osMC.setAccuracy(elem.getAttribute("accuracy"));
														osM.push(osMC);
													}
												}
											}
											hOs.push(osM);
										}
										else if (ndName == "osclass") {
											// parsing OSclass section
											OsClass osC = new OsClass();
											osC.setType(elem.getAttribute("type"));
											osC.setVendor(elem.getAttribute("vendor"));
											osC.setOsFamily(elem.getAttribute("osfamily"));
											osC.setOsGen(elem.getAttribute("osgen"));
											osC.setAccuracy(elem.getAttribute("accuracy"));
											hOs.push(osC);

										}
										else if(ndName == "portused") {
											// parsing Portused section
											PortUsed prU = new PortUsed(); 
											prU.setState(elem.getAttribute("state"));
											prU.setProto(elem.getAttribute("proto"));
											prU.setPortID(elem.getAttribute("portid"));
											hOs.push(prU);
										}
									}
								}
							}
							else if (ndName == "ports") {
								// creating nodelist for host's ports
								if (node2.hasChildNodes()) {
									nodel2 = node2.getChildNodes();
									for (int k = 0 ; k < nodel2.getLength() ; k++) {
										node2 = nodel2.item(k);
										ndName = node2.getNodeName();
										elem = (Element) node2 ;
										if (ndName == "extraports") {
											// parsing an extraports section
											ExtraPorts exP = new ExtraPorts();
											exP.setState(elem.getAttribute("state"));
											exP.setCount(elem.getAttribute("count"));
											if (node2.hasChildNodes()) {
												node2 = node2.getFirstChild();
												ndName = node2.getNodeName();
												if (ndName == "extrareasons") {
													// parsing extrareasons section for extraports
													elem = (Element) node2;
													ExtraReasons exR = new ExtraReasons();
													exR.setReason(elem.getAttribute("reason"));
													exR.setCount(elem.getAttribute("count"));
													exP.setReasons(exR);
												}
											}
											ports.push(exP);
										}
										else if (ndName == "port") {
											// parsing a port section
											Port port = new Port();
											port.setProtocol(elem.getAttribute("protocol"));
											port.setPortId(elem.getAttribute("portid"));
											if (node2.hasChildNodes()) {
												NodeList nodel3 = node2.getChildNodes();
												for (int z = 0 ; z < nodel3.getLength() ; z++) {
													node2 = nodel3.item(z);
													ndName = node2.getNodeName();
													elem = (Element) node2 ;
													if (ndName == "state") {
														// parsing port state section
														PortState portSt = new PortState();
														portSt.setState(elem.getAttribute("state"));
														portSt.setReason(elem.getAttribute("reason"));
														portSt.setReasonTtl(elem.getAttribute("reason_ttl"));
														port.setState(portSt);

													}
													else if (ndName == "service") {
														// parsing port service section
														PortService portSe = new PortService();
														portSe.setName(elem.getAttribute("name"));
														portSe.setProduct(elem.getAttribute("product"));
														portSe.setVersion(elem.getAttribute("version"));
														
														portSe.setExtraInfo(elem.getAttribute("extrainfo"));
														portSe.setMethod(elem.getAttribute("method"));
														portSe.setConf(elem.getAttribute("conf"));
														port.setService(portSe);
													}
												}
											}
											ports.push(port);
										}
									}
								}
							}
							else if(ndName == "hostnames") {
								// creating child nodelist for hostnames
								if (node2.hasChildNodes()) {
									nodel2 = node2.getChildNodes();
									for (int k = 0 ; k < nodel2.getLength() ; k++) {
										node2 = nodel2.item(k);
										ndName = node2.getNodeName();
										if (ndName == "hostname") {
											// parsing a hostname
											HostName hname = new HostName();
											elem = (Element) node2 ;
											hname.setName(elem.getAttribute("name"));
											hname.setType(elem.getAttribute("type"));
											hNames.push(hname);
										}
									}
								}
							}
							else if(ndName == "trace") {
								// parsing a trace section
								trace.setPort(elem.getAttribute("port"));
								trace.setProto(elem.getAttribute("proto"));
								// creating child nodelist for traced hops
								if (node2.hasChildNodes()) {
									nodel2 = node2.getChildNodes();
									for (int k = 0 ; k < nodel2.getLength() ; k++) {
										node2 = nodel2.item(k);
										ndName = node2.getNodeName();
										elem = (Element) node2 ;
										if (ndName == "hop") {
											// parsing a hop section
											Hop hop = new Hop();
											hop.setTtl(elem.getAttribute("ttl"));
											hop.setIpaddr(elem.getAttribute("ipaddr"));
											hop.setRtt(elem.getAttribute("rtt"));
											hop.setHost(elem.getAttribute("host"));
											trace.push(hop);
										}
									}
								}
							}
						}
					}
				}
				else if (ndName == "runstats") {
					Runstats rst = new Runstats();
					// creating child-nodes list for runstats
					if (node.hasChildNodes()) {
						nodel2 = node.getChildNodes();
						for (int k = 0 ; k < nodel2.getLength() ; k++) {
							node2 = nodel2.item(k);
							ndName = node2.getNodeName();
							elem = (Element) node2;
							if (ndName == "hosts") {
								// parsing a hosts section
								RunstatsHosts rhosts = new RunstatsHosts();
								rhosts.setDown(elem.getAttribute("down"));
								rhosts.setUp(elem.getAttribute("up"));
								rhosts.setTotal(elem.getAttribute("total"));
								rst.setRhosts(rhosts);
								break;
							}
						}
					}
					result.setRunstats(rst);
				}
			}
			ResultQueue rq = ResultQueue.getInstance();
			Result r = new Result(hashkey,result,id,executed); // setting values to this nmap result
			rq.push(r);					// pushing parsed result to the result queue
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
