package myflcikr;



import java.sql.Timestamp;
import java.util.Date;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * JDO-annotated model class for storing movie properties; movie's promotional
 * image is stored as a Blob (large byte array) in the image field.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Photo {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String title;

    @Persistent
    @Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    private String imageType;

    @Persistent
    private Blob image;
    
    @Persistent(valueStrategy = IdGeneratorStrategy.SEQUENCE)
    private int id;
    
    @Persistent
    private String userName;
    
    @Persistent
    private  Date time;
    
    
    public Key getKey() {
        return key;
    }

    public Photo(String title, byte[] image,String imageType, String userName, Date time)
    {
    	this.title=title;
    	this.image=new Blob(image);
    	this.imageType=imageType;
    	this.userName=userName;
    	this.time=time;
    }

    public long getId() {
        return key.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getImageType() {
        return imageType;
    }

    public byte[] getImage() {
        if (image == null) {
            return null;
        }

        return image.getBytes();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImage(byte[] bytes) {
        this.image = new Blob(bytes);
    }

    public int getqueryId()
    {
    	return id;
    }
    
    public Blob getBlob()
    {
    	return image;
    }
    public String gerUserName()
    {
    	return userName;
    }
    public Date getTime()
    {
    	return time;
    }
}
