package com.miyue.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * xml解析为VO工具类
 *
 * @author spring.tu
 * @version 1.0 Dec 13, 2010
 * 
 * 针对预研出的xml进行xmlutil类的更新：
 * （1）直接调用 XmlUtil.fromXML 拿到对应的EC2/S3 VO类
 * （2）xml节点为空值，对应VO值为空字符串
 * （3）xml节点不存在，对应VO值为null
 * （4）在deleteExcess方法中去掉预研给出多余的region节点
 * （5）下面main方法里面有实际调用例子
 * （6）如果是其他项目需求，请针对细节修改即可
 */
public class XmlUtil
{
    /**
     * item节点的统计
     */
    private static Set<String> indexList;

    /**
     * 初始化
     * 
     * @author tianjun tkf36897
     * @version 1.0 Dec 13, 2010
     */
    private static void init()
    {
        indexList = new HashSet<String>(0);
    }

    /**
     * (***)将xml转化为VO操作
     * 
     * 具体调用例子看下面main方法
     * 
     * @param xml 传入的xml
     * @param objClass 需要转换的类Class实例
     * @param itemClass 其中item的类Class实例，请按照顺序传入
     * @return 转换后的VO类
     * @throws UspMonitorException UspMonitorException
     * @version 1.0 Dec 13, 2010
     */
    @SuppressWarnings("unchecked")
    public static Object fromXML(String xml, Class objClass, Class... itemClass) throws Exception
    {
        init();
        XStream stream = new XStream(new DomDriver());
        if(itemClass != null && itemClass.length != 0)
        {
            for (int i = 0; i < itemClass.length; i++)
            {
                stream.alias("item" + i, itemClass[i]);
            }
        }

        String resultXml = null;
        try
        {
            Document document = DocumentHelper.parseText(deleteExcess(xml));
            Element root = document.getRootElement();
            root.setQName(new QName(objClass.getName()));
            changeTime(root.elements());
            changeItem(root.elements());
            resultXml = root.asXML();
        } catch (DocumentException e)
        {
//            UspLog.error("XmlUtil.fromXML():" + e.getMessage(), e);
            throw new Exception(
                    "XmlUtil.fromXML():DocumentException " + e.getMessage(), e);
        }

        return stream.fromXML(resultXml);
    }

    /**
     * 将item节点进行改名操作
     * 
     * 便于后面进行一对一的解析
     * @param elementList
     * @version 1.0 Dec 13, 2010
     */
    @SuppressWarnings("unchecked")
    private static void changeItem(List<Element> elementList)
    {
        Element tempElement;

        for (int i = 0; i < elementList.size(); i++)
        {
            tempElement = elementList.get(i);
            if (tempElement.getQName().getName().toLowerCase().equals("item"))
            {
                indexList.add(tempElement.getParent().getQName().getName());
                tempElement
                        .setQName(new QName("item" + (indexList.size() - 1)));
            }
        }

        for (int i = 0; i < elementList.size(); i++)
        {
            changeItem(elementList.get(i).elements());
        }
    }

    /**
     * 去除多余的节点
     * 
     * @param xmlString
     * @return
     * @version 1.0 Dec 13, 2010
     */
    private static String deleteExcess(String xmlString)
    {
        return xmlString.replaceAll("(<region>).*(</region>)", "");
    }

    /**
     * 进行time节点的对应修改
     * 
     * @param elementList
     * @version 1.0 Dec 13, 2010
     */
    @SuppressWarnings("unchecked")
    private static void changeTime(List<Element> elementList)
    {
        Date time = null;
        Calendar calendar;
        String tempTime = null;
        Element tempElement;
        Element timeElement;
        Element tempParentElement = null;

        for (int i = 0; i < elementList.size(); i++)
        {
            tempElement = elementList.get(i);
            if (tempElement.getQName().getName().toLowerCase().contains("time"))
            {
                // 下面的time换成我之前写的switchTime
                time = new Date();
                calendar = new GregorianCalendar();
                calendar.setTime(time);
                tempTime = calendar.toString().split("\\[")[1].split(",")[0]
                        .split("=")[1];
                tempParentElement = tempElement.getParent();
                tempParentElement.remove(tempElement);
                tempElement = new DefaultElement(tempElement.getQName()
                        .getName());
                timeElement = new DefaultElement("time");
                timeElement.setText(tempTime);
                tempElement.add(timeElement);
                tempParentElement.add(tempElement);
            }
        }

        if (time == null)
        {
            for (int i = 0; i < elementList.size(); i++)
            {
                changeTime(elementList.get(i).elements());
            }
        }
    }
    
    
    
//    /**
//     * 
//     * 
//     * @author tianjun tkf36897
//     * @param args
//     * @version 1.0 Dec 13, 2010
//     * @throws UspMonitorException 
//     */
//    public static void main(String[] args) throws UspMonitorException
//    {
//        String temp2 = "<DescribeSnapshotsResponse xmlns=\"http://ec2.amazonaws.com/doc/2010-08-31/\">"
//            + "<requestId>tianjun_requestId</requestId>"
//            + "<region>tianjun_requestId</region>"
//            + "<snapshotSet>"
//            + "<item>"
//            + "<snapshotId>110110snapId</snapshotId>"
//            + "<volumeId>33434volumeId</volumeId>"
//            + "<status>started</status>"
//            + "<startTime>2010-07-29T04:12:01.000Z</startTime>"
//            + "<progress>10%</progress>"
//            + "<ownerId>tianjun</ownerId>"
//            + "<volumeSize>33</volumeSize>"
//            + "<description>setDescription</description>"
//            + "<ownerAlias>setOwnerAlias</ownerAlias>"
//            + "<tagSet>"
//            + "  <item>"
//            + "    <key>setkey</key>"
//            + "    <value>setValue</value>"
//            + "  </item>"
//            + "  <item>"
//            + "    <key>tep2</key>"
//            + "    <value>dddValue</value>"
//            + "  </item>"
//            + "  <item>"
//            + "    <key>tep2</key>"
//            + "    <value>dddValue</value>"
//            + "  </item>"
//            + "</tagSet>"
//            + "</item>"
//            + "<item>"
//            + "<snapshotId>110110snapId</snapshotId>"
//            + "<volumeId>33434volumeId</volumeId>"
//            + "<status>started</status>"
//            + "<startTime>2010-07-29T04:12:01.000Z</startTime>"
//            + "<progress>10%</progress>"
//            + "<ownerId>tianjun</ownerId>"
//            + "<volumeSize>33</volumeSize>"
//            + "<description></description>"
//            + "<ownerAlias>setOwnerAlias</ownerAlias>"
//            + "<tagSet>"
//            + "  <item>"
//            + "    <key>setkey</key>"
//            + "    <value>setValue</value>"
//            + "  </item>"
//            + "  <item>"
//            + "    <key>tep2</key>"
//            + "    <value>dddValue</value>"
//            + "  </item>"
//            + "  <item>"
//            + "    <key>tep2</key>"
//            + "    <value>dddValue</value>"
//            + "  </item>"
//            + "</tagSet>"
//            + "</item>" + "</snapshotSet>" + "</DescribeSnapshotsResponse>";
//        
//        DescribeSnapshotsResponseType temp33 = (DescribeSnapshotsResponseType) fromXML(
//                temp2, DescribeSnapshotsResponseType.class,DescribeSnapshotsSetItemResponseType.class,ResourceTagSetItemType.class);
//        System.out.println(temp33.getSnapshotSet()[1].getDescription());
//    }
}
