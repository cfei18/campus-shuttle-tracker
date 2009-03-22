package com.nextdoorhacker.shuttletracker;

public class RSSItem 
{
    private String _title = null;
    private String _description = null;
    private String _link = null;
    private String _category = null;
    private String _pubdate = null;
    private String _point = null;
    private String _speed = null;
    private String _direction = null;
    
    RSSItem()
    {
    }
    void setTitle(String title)
    {
        _title = title;
    }
    void setDescription(String description)
    {
        _description = description;
    }
    void setLink(String link)
    {
        _link = link;
    }
    void setCategory(String category)
    {
        _category = category;
    }
    void setPubDate(String pubdate)
    {
        _pubdate = pubdate;
    }
    String getTitle()
    {
        return _title;
    }
    String getDescription()
    {
        return _description;
    }
    String getLink()
    {
        return _link;
    }
    String getCategory()
    {
        return _category;
    }
    String getPubDate()
    {
        return _pubdate;
    }
    public String toString()
    {
        // limit how much text you display
        if (_title.length() > 42)
        {
            return _title.substring(0, 42) + "...";
        }
        return _title;
    }
	/**
	 * @return the _point
	 */
	public String getPoint() {
		return _point;
	}
	/**
	 * @param _point the _point to set
	 */
	public void setPoint(String _point) {
		this._point = _point;
	}
	/**
	 * @return the _speed
	 */
	public String getSpeed() {
		return _speed;
	}
	/**
	 * @param _speed the _speed to set
	 */
	public void setSpeed(String _speed) {
		this._speed = _speed;
	}
	/**
	 * @return the _direction
	 */
	public String getDirection() {
		return _direction;
	}
	/**
	 * @param _direction the _direction to set
	 */
	public void setDirection(String _direction) {
		this._direction = _direction;
	}
}
