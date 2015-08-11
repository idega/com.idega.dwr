package com.idega.dwr.convert;

import org.directwebremoting.convert.JDOMConverter;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

public class JDocumentConverter extends JDOMConverter {

	public Object convertInbound(Class<?> paramType, InboundVariable data, InboundContext inctx) {
//		if (data.isNull()) {
//            return null;
//        }
//
//		String value = LocalUtil.urlDecode(data.getValue());
//		try {
//			Document doc = XmlUtil.getJDOMXMLDocument(value);
//
//			if (paramType == Document.class) {
//                return doc;
//            } else if (paramType == Element.class) {
//                return doc.getRootElement();
//            }
//
//            throw new ConversionException(paramType);
//		} catch (ConversionException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new ConversionException(paramType, e);
//		}

		throw new UnsupportedOperationException();
	}

	 @Override
	public OutboundVariable convertOutbound(Object data, OutboundContext outctx) {
//		 try {
//			 Format outformat = Format.getRawFormat();
//	         outformat.setEncoding(CoreConstants.ENCODING_UTF8);
//
//	         StringWriter xml = new StringWriter();
//             XMLOutputter writer = new XMLOutputter(outformat);
//
//	         // Using XSLT to convert to a stream. Setup the source
//	         boolean document = data instanceof Document;
//             if (document) {
//	             writer.output((Document) data, xml);
//	         } else if (data instanceof Element) {
//	             writer.output((Element) data, xml);
//	         } else {
//	             throw new ConversionException(data.getClass());
//	         }
//
//	         xml.flush();
//	         String xmlString = xml.toString();
//             String script = document ? EnginePrivate.xmlStringToJavascriptDomDocument(xmlString) :
//            	 						EnginePrivate.xmlStringToJavascriptDomElement(xmlString);
//
//	         OutboundVariable ov = new NonNestedOutboundVariable(script);
//	         outctx.put(data, ov);
//	         return ov;
//	     } catch (ConversionException ex)  {
//	         throw ex;
//	     } catch (Exception ex) {
//	         throw new ConversionException(data.getClass(), ex);
//	     }

		 throw new UnsupportedOperationException();
	 }

}