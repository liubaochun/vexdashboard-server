function change_layout(a){$("body").toggleClass(a),$.AdminLTE.layout.fixSidebar()}function change_skin(a){return $.each(my_skins,function(a){$("body").removeClass(my_skins[a])}),$("body").addClass(a),store("skin",a),!1}function store(a,l){"undefined"!=typeof Storage?localStorage.setItem(a,l):alert("Please use a modern browser to properly view this template!")}function get(a){return"undefined"!=typeof Storage?localStorage.getItem(a):void alert("Please use a modern browser to properly view this template!")}function setup(){var a=get("skin");a&&$.inArray(a,my_skins)&&change_skin(a)}var my_skins=["skin-blue","skin-black","skin-red","skin-yellow","skin-purple","skin-green"];$(function(){var a=$("<div />").css({position:"fixed",top:"70px",right:"0",background:"#fff","border-radius":"5px 0px 0px 5px",padding:"10px 15px","font-size":"16px","z-index":"99999",cursor:"pointer",color:"#3c8dbc","box-shadow":"0 1px 3px rgba(0,0,0,0.1)"}).html("<i class='fa fa-gear'></i>").addClass("no-print"),l=$("<div />").css({padding:"10px",position:"fixed",top:"70px",right:"-250px",background:"#fff",border:"0px solid #ddd",width:"250px","z-index":"99999","box-shadow":"0 1px 3px rgba(0,0,0,0.1)"}).addClass("no-print");l.append("<h4 class='text-light-blue' style='margin: 0 0 5px 0; border-bottom: 1px solid #ddd; padding-bottom: 15px;'>Layout Options</h4><div class='form-group'><div class='checkbox'><label><input type='checkbox' onchange='change_layout(\"fixed\");'/> Fixed layout</label></div></div><div class='form-group'><div class='checkbox'><label><input type='checkbox' onchange='change_layout(\"layout-boxed\");'/> Boxed Layout</label></div></div><div class='form-group'><div class='checkbox'><label><input type='checkbox' onchange='change_layout(\"sidebar-collapse\");'/> Collapsed Sidebar</label></div></div>");var e=$("<ul />",{"class":"list-unstyled"}),t=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-blue\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div><span style='display:block; width: 20%; float: left; height: 10px; background: #367fa9;'></span><span class='bg-light-blue' style='display:block; width: 80%; float: left; height: 10px;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Blue</p></a>");e.append(t);var s=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-black\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div style='box-shadow: 0 0 2px rgba(0,0,0,0.1)' class='clearfix'><span style='display:block; width: 20%; float: left; height: 10px; background: #fefefe;'></span><span style='display:block; width: 80%; float: left; height: 10px; background: #fefefe;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Black</p></a>");e.append(s);var i=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-purple\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div><span style='display:block; width: 20%; float: left; height: 10px;' class='bg-purple-active'></span><span class='bg-purple' style='display:block; width: 80%; float: left; height: 10px;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Purple</p></a>");e.append(i);var p=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-green\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div><span style='display:block; width: 20%; float: left; height: 10px;' class='bg-green-active'></span><span class='bg-green' style='display:block; width: 80%; float: left; height: 10px;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Green</p></a>");e.append(p);var d=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-red\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div><span style='display:block; width: 20%; float: left; height: 10px;' class='bg-red-active'></span><span class='bg-red' style='display:block; width: 80%; float: left; height: 10px;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Red</p></a>");e.append(d);var o=$("<li />",{style:"float:left; width: 50%; padding: 5px;"}).append("<a href='#' onclick='change_skin(\"skin-yellow\")' style='display: block; box-shadow: -1px 1px 2px rgba(0,0,0,0.0);' class='clearfix full-opacity-hover'><div><span style='display:block; width: 20%; float: left; height: 10px;' class='bg-yellow-active'></span><span class='bg-yellow' style='display:block; width: 80%; float: left; height: 10px;'></span></div><div><span style='display:block; width: 20%; float: left; height: 40px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 40px; background: #f4f5f7;'></span></div><p class='text-center'>Skin Yellow</p></a>");e.append(o),l.append("<h4 class='text-light-blue' style='margin: 0 0 5px 0; border-bottom: 1px solid #ddd; padding-bottom: 15px;'>Skins</h4>"),l.append(e),a.click(function(){$(this).hasClass("open")?($(this).animate({right:"0"}),l.animate({right:"-250px"}),$(this).removeClass("open")):($(this).animate({right:"250px"}),l.animate({right:"0"}),$(this).addClass("open"))}),$("body").append(a),$("body").append(l),setup()});