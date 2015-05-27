# Introduction #

It is necessary to put some work into understanding the site you want to integrate using cs-iframe. Especially how login and the logout are handled.

# Configuration #

First CS-IFrame has to know where the target site is. The value provided to the "iFrame Source URL"-field is usually the link to where the login form post it's content.

![http://oppna-program-cv-iframe.googlecode.com/svn/wiki/CS-configuration.png](http://oppna-program-cv-iframe.googlecode.com/svn/wiki/CS-configuration.png)

To activate the credential functionality in cs-iframe you have to check "Authenticate" and select a [SiteKey](CSAdmin.md).

You have to configure the field-name for username and password. CS-IFrame provides the values for these fields as dynamic data provided for the user logged into the portal.

For many services this information is not enough, the login form contains hidden field that are necessary to be able to login. This "extra" information can be provided as static key/value pairs as "Hidden variables"

> "key1=value1&key2=value2"

Only static information can be provided in the hidden variables field.

## Variations ##

### SSL URL's-only ###

This option convert all links produced by cs-iframe to https. It can remove some browser warnings.

### Explicit Logout ###

Some external sites expect new users to always have a new browser window, or they trust a stored cookie or generally misbehaves during login - creating a risk of exposing information from one user to another. This are badly designed sites and need special handeling.

Cs-iframe allows for one GET-action to be performed before it tries to login. Usually these crappy sites have managed to implement a one action logout. So, find the sites logout and supply that URL in the "Task before iFrame"-field.

### Add user logged in ###

Some sites can make uses of the userId of the person logged into the portal. Checking this option will add this information to the iFrame src when calling the site.

### A site that breaks out from iFrames ###

Sites can be written to not allow them to be displayed inside an IFrame. If the site is written in this way cs-iframe cannot be used.

### InternetExplorer (IE) and P3P ###

Internet Explorer has a much more restrictive policy for showing content in IFrames. If the content is from a domains different than the one serving the iFrame, IE will forbid it.

However, there is a way around this restriction. If the external site includes P3P-Header the content will be allowed.

# How it works #

Cs-IFrame creates a virtual login form containing the information provided in the configuration. This form is loaded into the IFrame<sup>1</sup> and then automatically submitted to the same adress as the site we want to integrate does<sup>2</sup>.

![http://oppna-program-cv-iframe.googlecode.com/svn/wiki/CS-how_it_work.png](http://oppna-program-cv-iframe.googlecode.com/svn/wiki/CS-how_it_work.png)

The reply from the external site are then caught inside the portlet iFrame<sup>3</sup> and since the action we just did was login, we are logged into the site and can continue to interact with is as a logged in user<sup>4</sup>.