package kr.co.watchpoint.evcms.struct;

public class LocalAuthorizationList 
{
	public LOCAL_AUTHORIZATION[] localAuthorizationList;
	
    public class LOCAL_AUTHORIZATION 
    {
    	public String idTag;
    	public ID_TAG_INFO idTagInfo;
    }
    
    public class ID_TAG_INFO 
    {
    	public String expiryDate;
    	public String parentIdTag;
    	public String status;
    }
  	
}
