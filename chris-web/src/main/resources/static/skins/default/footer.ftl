<ul class="breadcrumb">
	<#list links as link>
	<li>
		<a href="${link.url}" target="_blank">${link.name}</a>
		<#if link_has_next><span class="divider">|</span></#if>
	</li>
	</#list>
</ul>
 <footer class="footer">
   <p class="pull-right"><a href="#">Back to top</a></p>
   <p>
   	<a href="#" target="_blank">chris ${cfg.version}</a> 的诞生要感谢众多软件和开源项目.
   	<a href="http://www.java.com" target="_blank">java</a>, 
   	<a href="http://www.springsource.org/spring-framework" target="_blank">springframework</a>, 
   	<a href="http://twitter.github.com/bootstrap" target="_blank">bootstrap</a>, 
   	<a href="http://freemarker.org" target="_blank">freemarker</a>, 
   	<a href="http://www.b3log.org" target="_blank">b3log</a> 
   	…
   </p>
   <p>Code licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License v2.0</a>. Documentation licensed under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
   <p>&copy; 2020 <a href="http://www.noday.net" target="_blank">noday</a></p>
 </footer>
<script src="${contextPath}/js/jquery-1.8.2.js"></script>
<script src="${contextPath}/js/bootstrap.js"></script>
<script src="${contextPath}/js/jquery.qrcode.min.js"></script>
<script src="${contextPath}/js/utils.js"></script>
<script type="text/javascript">
<#if (cfg.duoshuoKey)!""=="">
<!-- Duoshuo Comment is close -->
<!-- 试试https://beaudar.lipk.org/ -->
<!-- Duoshuo Comment END -->
</#if>
$(function() {
	$('.carousel').carousel();
	$('#qrcode').qrcode({width: 188,height: 188,text: window.location.href});
});
</script>
<div style="display: none;">${cfg.hiddenSource}</div>
