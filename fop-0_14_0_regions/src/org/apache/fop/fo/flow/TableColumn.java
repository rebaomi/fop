/*-- $Id$ -- 

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================
 
    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:
 
 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.
 
 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.
 
 4. The names "FOP" and  "Apache Software Foundation"  must not be used to
    endorse  or promote  products derived  from this  software without  prior
    written permission. For written permission, please contact
    apache@apache.org.
 
 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.
 
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation and was  originally created by
 James Tauber <jtauber@jtauber.com>. For more  information on the Apache 
 Software Foundation, please see <http://www.apache.org/>.
 
 */

package org.apache.fop.fo.flow;

// FOP
import org.apache.fop.fo.*;
import org.apache.fop.fo.properties.*;
import org.apache.fop.layout.*;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.*;

public class TableColumn extends FObj {
	
    FontState fs;
    ColorType backgroundColor;
    ColorType borderColor;
    int borderWidth;
    int borderStyle;
    int columnWidth;
    int columnOffset;
    
    AreaContainer areaContainer;
    
    public static class Maker extends FObj.Maker {
	public FObj make(FObj parent, PropertyList propertyList)
	    throws FOPException {
	    return new TableColumn(parent, propertyList);
	}
    }

    public static FObj.Maker maker() {
	return new TableColumn.Maker();
    }

    public TableColumn(FObj parent, PropertyList propertyList) {
	super(parent, propertyList);
	this.name = "fo:table-column";
    }

    public int getColumnWidth() {
        return columnWidth;
    }

    public int getColumnNumber() {
	return 0; // not implemented yet
    }
    
    public Status layout(Area area) throws FOPException {
	if (this.marker == BREAK_AFTER) {
	    return new Status(Status.OK);
	}

	if (this.marker == START) {
	    String fontFamily =
		this.properties.get("font-family").getString(); 
	    String fontStyle =
		this.properties.get("font-style").getString(); 
	    String fontWeight =
		this.properties.get("font-weight").getString(); 
	    int fontSize =
		this.properties.get("font-size").getLength().mvalue(); 
	    
	    this.fs = new FontState(area.getFontInfo(), fontFamily, 
				    fontStyle, fontWeight, fontSize);  
	    this.backgroundColor =
		this.properties.get("background-color").getColorType();
	    this.borderColor =
		this.properties.get("border-color").getColorType();
	    this.borderWidth =
		this.properties.get("border-width").getLength().mvalue();
	    this.borderStyle =
		this.properties.get("border-style").getEnum();
            this.columnWidth = 
                this.properties.get("column-width").getLength().mvalue();
            
            // initialize id                       
            String id = this.properties.get("id").getString();            
            area.getIDReferences().initializeID(id,area);                                
        }

	this.areaContainer =
	    new AreaContainer(fs, columnOffset - area.borderWidthLeft, -area.borderWidthTop, columnWidth, 
			  area.getHeight(), Position.RELATIVE);
	areaContainer.setPage(area.getPage());
	areaContainer.setBackgroundColor(backgroundColor);
        areaContainer.setBorderStyle(borderStyle, borderStyle, borderStyle, borderStyle); 
        areaContainer.setBorderWidth(borderWidth, borderWidth, borderWidth, borderWidth); 
        areaContainer.setBorderColor(borderColor, borderColor, borderColor, borderColor); 
        areaContainer.setHeight(area.getHeight());
	area.addChild(areaContainer);
        
	return new Status(Status.OK);
    }
    
    public void setColumnOffset(int columnOffset) {
      this.columnOffset = columnOffset; 
    }
    
    public void setHeight(int height) {
	areaContainer.setMaxHeight(height);
	areaContainer.setHeight(height);
    }
    
}