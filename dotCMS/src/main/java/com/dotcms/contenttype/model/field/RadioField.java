package com.dotcms.contenttype.model.field;

import java.util.Collection;
import java.util.List;

import org.immutables.value.Value;

import com.google.common.collect.ImmutableList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import static com.dotcms.util.CollectionsUtils.list;

@JsonSerialize(as = ImmutableRadioField.class)
@JsonDeserialize(as = ImmutableRadioField.class)
@Value.Immutable
public abstract class RadioField extends SelectableValuesField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public  Class type() {
		return  RadioField.class;
	}

	@JsonIgnore
	@Value.Derived
	@Override
	public List<DataTypes> acceptedDataTypes(){
		return ImmutableList.of(DataTypes.TEXT, DataTypes.BOOL, DataTypes.FLOAT,DataTypes.INTEGER);
	}
	@Value.Default
	@Override
	public DataTypes dataType(){
		return DataTypes.TEXT;
	};
	

	
	public abstract static class Builder implements FieldBuilder {}

	@JsonIgnore
	public Collection<ContentTypeFieldProperties> getFieldContentTypeProperties(){
		return list(ContentTypeFieldProperties.NAME, ContentTypeFieldProperties.REQUIRED,
				ContentTypeFieldProperties.VALUES, ContentTypeFieldProperties.DEFAULT_VALUE,
				ContentTypeFieldProperties.HINT, ContentTypeFieldProperties.SEARCHABLE, ContentTypeFieldProperties.INDEXED,
				ContentTypeFieldProperties.LISTED, ContentTypeFieldProperties.DATA_TYPE);
	}
}
