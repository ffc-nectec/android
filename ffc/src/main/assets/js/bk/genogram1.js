var svgns = "http://www.w3.org/2000/svg"
function genogramSymbol(data, spouse) {
    this.data = data;
    this.spouse = spouse;
    this.lstObjLevel = [];
    this.showPicture = false;
    this.CMale = function (idName, x, y) {
        var obj = document.getElementById(idName);
        if (obj == null) {
            var data = this.GetDataById(idName);
            if (data.length > 0) {
                if (data[0].status === "live") {
                    this.Male(data[0].id, x, y);
                }
                if (data[0].status === "dead") {
                    this.MaleDeath(data[0].id, x, y);
                }
            }
        }
    }
    this.CFemale = function (idName, x, y) {
        var obj = document.getElementById(idName);
        if (obj == null) {
            var data = this.GetDataById(idName);
            if (data.length > 0) {
                if (data[0].status === "live") {
                    this.Female(data[0].id, x, y);
                }
                if (data[0].status === "dead") {
                    this.FemaleDeath(data[0].id, x, y);
                }
                if (data[0].status === "Pregnancy") {
                    this.Pregnancy(data[0].id, x, y);
                }
                if (data[0].status === "MisCarriage") {
                    this.MisCarriage(data[0].id, x, y);
                }
            }
        }
    }
    this.Male = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'rect');
        obj.setAttributeNS(null, 'x', x);
        obj.setAttributeNS(null, 'y', y);
        obj.setAttributeNS(null, 'height', '50');
        obj.setAttributeNS(null, 'width', '50');
        obj.setAttributeNS(null, 'stroke-width', '2');
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

    }
    this.Female = function (idName, x, y) {
        var radios = 25;
        var obj = document.createElementNS(svgns, 'circle');
        obj.setAttributeNS(null, 'cx', x + radios);
        obj.setAttributeNS(null, 'cy', y + radios);
        obj.setAttributeNS(null, 'r', radios);
        obj.setAttributeNS(null, 'stroke-width', '2');
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);
    }
    this.Pet = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'polygon');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'cx', x);
        obj.setAttributeNS(null, 'cy', y);
        obj.setAttributeNS(null, 'points', x + "," + (y + height / 2) + " " + (x + width / 2) + "," + (y) + " " + (x + width / 2) + "," + (y) + " " + (x + width) + "," + (y + height / 2) + " " + (x + width / 2) + "," + (y + height));
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', '#000000');
        obj.setAttributeNS(null, 'stroke-width', 2);
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);
    }
    this.Pregnancy = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'polygon');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'cx', x);
        obj.setAttributeNS(null, 'cy', y);
        obj.setAttributeNS(null, 'points', x + "," + (y + height) + " " + (x + width / 2) + "," + (y) + " " + (x + width) + "," + (y + height));
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', '#000000');
        obj.setAttributeNS(null, 'stroke-width', 2);
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

    }
    this.MisCarriage = function (idName, x, y) {

        var obj = document.createElementNS(svgns, 'polygon');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'cx', x);
        obj.setAttributeNS(null, 'cy', y);
        obj.setAttributeNS(null, 'points', x + "," + (y + height) + " " + (x + width / 2) + "," + (y) + " " + (x + width) + "," + (y + height));
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', '#000000');
        obj.setAttributeNS(null, 'stroke-width', 2);
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', y);
        objLine.setAttributeNS(null, 'x2', x + width);
        objLine.setAttributeNS(null, 'y2', y + height);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, 'id', 'l' + idName);
        document.getElementById('svgOne').appendChild(objLine);

        var objLine2 = document.createElementNS(svgns, 'line');
        objLine2.setAttributeNS(null, 'x1', x);
        objLine2.setAttributeNS(null, 'y1', y + height);
        objLine2.setAttributeNS(null, 'x2', x + width);
        objLine2.setAttributeNS(null, 'y2', y);
        objLine2.setAttributeNS(null, 'stroke', 'black');
        objLine2.setAttributeNS(null, 'stroke-width', 2);
        objLine2.setAttributeNS(null, 'id', 'r' + idName);
        document.getElementById('svgOne').appendChild(objLine2);


    }
    this.Abortion = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'polygon');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'cx', x);
        obj.setAttributeNS(null, 'cy', y);
        obj.setAttributeNS(null, 'points', x + "," + (y + height) + " " + (x + width / 2) + "," + (y) + " " + (x + width) + "," + (y + height));
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', '#000000');
        obj.setAttributeNS(null, 'stroke-width', 2);
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', y);
        objLine.setAttributeNS(null, 'x2', x + width);
        objLine.setAttributeNS(null, 'y2', y + height);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        document.getElementById('svgOne').appendChild(objLine);

        var objLine2 = document.createElementNS(svgns, 'line');
        objLine2.setAttributeNS(null, 'x1', x);
        objLine2.setAttributeNS(null, 'y1', y + height);
        objLine2.setAttributeNS(null, 'x2', x + width);
        objLine2.setAttributeNS(null, 'y2', y);
        objLine2.setAttributeNS(null, 'stroke', 'black');
        objLine2.setAttributeNS(null, 'stroke-width', 2);
        document.getElementById('svgOne').appendChild(objLine2);

        var objLine3 = document.createElementNS(svgns, 'line');
        objLine3.setAttributeNS(null, 'x1', x);
        objLine3.setAttributeNS(null, 'y1', y + height / 2);
        objLine3.setAttributeNS(null, 'x2', x + width);
        objLine3.setAttributeNS(null, 'y2', y + height / 2);
        objLine3.setAttributeNS(null, 'stroke', 'black');
        objLine3.setAttributeNS(null, 'stroke-width', 2);
        document.getElementById('svgOne').appendChild(objLine3);
    }
    this.MaleDeath = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'rect');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'x', x);
        obj.setAttributeNS(null, 'y', y);
        obj.setAttributeNS(null, 'height', '50');
        obj.setAttributeNS(null, 'width', '50');
        obj.setAttributeNS(null, 'stroke-width', '2');
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', y);
        objLine.setAttributeNS(null, 'x2', x + width);
        objLine.setAttributeNS(null, 'y2', y + height);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, 'id', "l" + idName);
        document.getElementById('svgOne').appendChild(objLine);

        var objLine2 = document.createElementNS(svgns, 'line');
        objLine2.setAttributeNS(null, 'x1', x);
        objLine2.setAttributeNS(null, 'y1', y + height);
        objLine2.setAttributeNS(null, 'x2', x + width);
        objLine2.setAttributeNS(null, 'y2', y);
        objLine2.setAttributeNS(null, 'stroke', 'black');
        objLine2.setAttributeNS(null, 'stroke-width', 2);
        objLine2.setAttributeNS(null, 'id', "r" + idName);
        document.getElementById('svgOne').appendChild(objLine2);

    }
    this.FemaleDeath = function (idName, x, y) {
        var radios = 25;
        var obj = document.createElementNS(svgns, 'circle');
        obj.setAttributeNS(null, 'cx', x + radios);
        obj.setAttributeNS(null, 'cy', y + radios);
        obj.setAttributeNS(null, 'r', radios);
        obj.setAttributeNS(null, 'stroke-width', '2');
        obj.setAttributeNS(null, 'fill', '#FFFFFF');
        obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', y);
        objLine.setAttributeNS(null, 'x2', x + radios * 2);
        objLine.setAttributeNS(null, 'y2', y + radios * 2);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, 'id', 'l' + idName);
        document.getElementById('svgOne').appendChild(objLine);

        var objLine2 = document.createElementNS(svgns, 'line');
        objLine2.setAttributeNS(null, 'x1', x);
        objLine2.setAttributeNS(null, 'y1', y + radios * 2);
        objLine2.setAttributeNS(null, 'x2', x + radios * 2);
        objLine2.setAttributeNS(null, 'y2', y);
        objLine2.setAttributeNS(null, 'stroke', 'black');
        objLine2.setAttributeNS(null, 'stroke-width', 2);
        objLine2.setAttributeNS(null, 'id', 'r' + idName);
        document.getElementById('svgOne').appendChild(objLine2);
    }
    this.GenderUnknown = function (idName, x, y) {
        var obj = document.createElementNS(svgns, 'text');
        obj.setAttributeNS(null, 'x', x + x / 4);
        obj.setAttributeNS(null, 'y', y);
        obj.setAttributeNS(null, 'fill', '#000000');
        obj.textContent = "?";
        obj.setAttributeNS(null, 'font-size', '3em');
        obj.setAttributeNS(null, 'id', idName);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);
    }
    this.GetDataById = function (id) {
        var result = [];
        for (var i = 0; i < this.data.length; i++) {
            if (this.data[i]["id"] == id) {
                result.push(this.data[i]);
                break;
            }
        }
        return result;
    }
    this.GetChildDataById = function (father_id, mother_id) {

        var result = [];
        for (var i = 0; i < this.data.length; i++) {
            if (this.data[i]["father_id"] === father_id && this.data[i]["mother_id"] === mother_id) {
                result.push(this.data[i]);

            }
        }
        return result;

    }
    this.FindSpouse = function (id, spouse) {
        var slist = spouse;

        var result = [];
        for (var i = 0; i < slist.length; i++) {
            if (slist[i].id1 == id || slist[i].id2 == id) {
                result.push(slist[i]);
            }
        }
        //alert(JSON.stringify(result));
        return result;
    }
    this.CreateSpouse = function (id, list) {
        if (list != undefined) {
            var main = document.getElementById(id);
            var util = new Utility();
            var data = util.GetTopLeftXY(main);
            var x = 0;
            var y = 0;
            if (data.length > 0) {
                x = data[0].x;
                y = data[0].y;
            }
            for (var i = 0; i < list.length; i++) {
                var obj1 = document.getElementById(list[i].id1);
                var obj2 = document.getElementById(list[i].id2);

                if (obj1 == undefined && list[i].id1 != "") {
                    var p = this.GetDataById(list[i].id1);
                    if (p.length > 0) {
                        if (p[0].sex == "Male") {
                            this.CMale(p[0].id, 200, 300);
                        }
                        else if (p[0].sex == "Female") {
                            this.CFemale(p[0].id, 200, 300);
                        }
                    }
                }
                if (obj2 == undefined && list[i].id2 != "") {
                    var p = this.GetDataById(list[i].id2);
                    if (p.length > 0) {
                        if (p[0].sex == "Male") {
                            this.CMale(p[0].id, x + 150 * (i + 1), y);
                        }
                        else if (p[0].sex == "Female") {
                            this.CFemale(p[0].id, x - 150 * (i + 1), y);
                        }
                    }
                    // var link = new genogramRelationship(this.spouse);
                    // link.SpouseRelationship(main.id,list[i].id2);
                }
            }
        }
    }
    this.CreateRelationship = function (id) {
        var obj = this.GetDataById(id);
        var father_id = 0;
        var mother_id = 0;
        if (obj != null) {
            if (obj.length > 0) {
                father_id = obj[0].father_id;
                mother_id = obj[0].mother_id;
                var father = document.getElementById(father_id);
                var mother = document.getElementById(mother_id);
                var lineObj = document.getElementById("line" + father_id + "-" + mother_id);
                if (lineObj == null) {
                    lineObj = document.getElementById("line" + mother_id + "-" + father_id);
                }
                var x1 = 0, y1 = 0, x2 = 0, y2 = 0;
                if (lineObj != null) {

                    var lstChild = this.GetChildDataById(father_id, mother_id);
                    var step = (lineObj.x2.baseVal.value - lineObj.x1.baseVal.value) / (lstChild.length + 1);
                    x1 = lineObj.x1.baseVal.value;
                    y1 = lineObj.y1.baseVal.value;
                    var util = new Utility();
                    for (var i = 0; i < lstChild.length; i++) {
                        var name = "PC" + lstChild[i].id;
                        var objChild = document.getElementById(lstChild[i].id);

                        var pos = util.GetTopLeftXY(objChild);

                        if (pos.length > 0) {
                            if (objChild.constructor.name == "SVGRectElement") {
                                x2 = pos[0].x + objChild.width.baseVal.value / 2;
                                y2 = pos[0].y;
                            }
                            else if (objChild.constructor.name == "SVGCircleElement") {
                                x2 = pos[0].x;
                                y2 = pos[0].y;
                            }
                            else if (objChild.constructor.name == "SVGPolygonElement") {
                                x2 = pos[0].x + (objChild.points[1].x - objChild.points[0].x);
                                y2 = pos[0].y;
                            }

                            var personInfo = this.GetDataById(lstChild[i].id);
                            var objLine = document.createElementNS(svgns, 'line');
                            objLine.setAttributeNS(null, 'x1', x1 + ((1 + i) * step));
                            objLine.setAttributeNS(null, 'y1', y1);
                            objLine.setAttributeNS(null, 'x2', x2);
                            objLine.setAttributeNS(null, 'y2', y2);
                            objLine.setAttributeNS(null, 'stroke', 'black');
                            if (personInfo.length > 0) {
                                if (personInfo[0].child_type != null) {
                                    if (personInfo[0].child_type == "adopted_child") {
                                        objLine.setAttributeNS(null, 'stroke', 'blue');
                                        objLine.setAttributeNS(null, 'stroke-dasharray', '8 2 8 2');
                                    }
                                    else if (personInfo[0].child_type == "foster_child") {
                                        objLine.setAttributeNS(null, 'stroke', 'green');
                                        objLine.setAttributeNS(null, 'stroke-dasharray', '3 5 3 5');

                                    }
                                }
                            }

                            objLine.setAttributeNS(null, 'stroke-width', 2);
                            objLine.setAttributeNS(null, "id", "line" + name);
                            document.getElementById('svgOne').appendChild(objLine);
                        }
                    }
                }
            }
        }

    }

    this.GenerateGenogram = function (id) {
        var svg = document.getElementById('svgOne');
        var pt = svg.createSVGPoint();
        svg.addEventListener("mousemove", function (ev) {
            pt.x = ev.clientX; pt.y = ev.clientY;
            var loc = pt.matrixTransform(svg.getScreenCTM().inverse());
            document.title = "x:" + loc.x + " ,y:" + loc.y;
        });
        //this.CreateLineMidium();
        var level = 0;
        var result = this.GetDataById(id);
        var dataFather = this.GetDataById(result[0].father_id);
        if (dataFather != null) {
            this.CMale(dataFather[0].id, 200, 200);
            this.lstObjLevel.push(dataFather[0].id);
            this.GetFamilyDetail(dataFather[0].id, level + 1);
            // wait edit
            //this.lstObjLevelInt = this.lstObjLevel.map(x=>+x);
            // convert to interger
            this.lstObjLevelInt = this.lstObjLevel;
            this.lstObjLevelIntSort = this.lstObjLevelInt.sort(function (a, b) { return a - b });
            for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
                this.AdjustPosition(this.lstObjLevelIntSort[i], level);
                level = level + 1;
                // this.CreateRelationship(this.lstObjLevel[i])
            }
            var rel = new genogramRelationship(this.spouse);
            rel.CreateSpouseRelationship();
            for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
                this.CreateRelationship(this.lstObjLevelIntSort[i])
            }
        }
    }
    this.GetFamilyDetail = function (id, level, index) {
        // Spouses
        //alert(JSON.stringify(this.spouse));
        var main = document.getElementById(id);
        if (main == null || main == undefined) {
            var data = this.GetDataById(id);
            if (data.length > 0) {
                if (data[0].sex == "Male") {
                    this.CMale(id, 100 + (100 * index), (120 * (level + 1)));
                }
                else if (data[0].sex == "Female") {
                    this.CFemale(id, 100 + (100 * index), (120 * (level + 1)));
                }
            }
        }
        var spouselst = this.FindSpouse(id, this.spouse);
        if (spouselst.length > 0) {
            // var spouselst= this.FindSpouse(dataFather[0].id,this.spouse);
            this.CreateSpouse(id, spouselst);
            //this.AdjustPosition(id);

            //var obj = document.getElementById(id);
            //console.log(JSON.stringify(obj));
            //alert(obj);
            // Child

            for (var i = 0; i < spouselst.length; i++) {
                //this.GetFamilyDetail(spouselst[i].id,level+1);
                var dataChilds = this.GetChildDataById(spouselst[i].id1, spouselst[i].id2);
                if (dataChilds.length == 0) {
                    dataChilds = this.GetChildDataById(spouselst[i].id2, spouselst[i].id1);
                }
                if (dataChilds.length > 0) {
                    for (var j = 0; j < dataChilds.length; j++) {
                        this.GetFamilyDetail(dataChilds[j].id, level + 1, j);
                    }
                    this.lstObjLevel.push(dataChilds[0].id);
                }
            }
        }
        else {
            var data = this.GetDataById(id);
            if (data.length > 0) {
                if (data[0].sex == "Male") {
                    this.CMale(id, 100 + (100 * index), 200 + (100 * level));
                }
                else if (data[0].sex == "Female") {
                    this.CFemale(id, 100 + (100 * index), 200 + (100 * level));
                }
            }
        }
    }
    this.AdjustPosition = function (id, level) {

        var x1 = 0;
        var y1 = 0;
        var x2 = 0;
        var y2 = 0;

        var obj = document.getElementById(id);
        var util = new Utility();
        var data = util.GetTopLeftXY(obj);
        var objlst = [];
        if (data.length > 0) {
            x1 = data[0].x;
            y1 = data[0].y;
        }
        if (obj != null) {
            var lst = obj.nextSibling.ownerSVGElement.childNodes;
            for (var i = 0; i < lst.length; i++) {
                x2 = 0;
                y2 = 0;
                if (lst[i].constructor.name == "SVGRectElement") {
                    var data = util.GetTopLeftXY(lst[i]);
                    if (data.length > 0) {
                        y2 = data[0].y;
                    }
                } else if (lst[i].constructor.name == "SVGCircleElement") {
                    var data = util.GetTopLeftXY(lst[i]);
                    if (data.length > 0) {
                        y2 = lst[i].cy.baseVal.value - lst[i].r.baseVal.value;
                    }
                } else if (lst[i].constructor.name == "SVGPolygonElement") {
                    var data = util.GetTopLeftXY(lst[i]);
                    if (data.length > 0) {
                        y2 = data[0].y;
                    }
                }
                if (y1 == y2) {
                    objlst.push(lst[i]);
                }
                //console.log(lst[i].constructor.name);
            }
            var lstParent = [];
            for (var i = 0; i < objlst.length; i++) {
                var id = objlst[i].id;
                var personData = this.GetDataById(id);
                if (personData.length > 0) {
                    lstParent.push({
                        "data": personData[0].father_id + "," + personData[0].mother_id
                    });
                }
            }
            //           Cut duplicate
            // var tmp = Array.from(new Set(lstParent.map(s=>s.data)))
            //     .map((data)=>{return {
            //     data:data
            // }});
            var tmp = lstParent;
            var lstSameLevel = [];
            for (var j = 0; j < tmp.length; j++) {
                lstSameLevel = [];
                var lineX1 = 0, lineX2 = 0, lineY1 = 0, lineY2 = 0;
                var father_id = 0, mother_id = 0;
                father_id = tmp[j].data.split(',')[0];
                mother_id = tmp[j].data.split(',')[1];
                var lstChildsAndSpouse = this.GetlstChildsAndSpouse(father_id, mother_id);
                var util = new Utility();
                var father = document.getElementById(father_id);
                var mother = document.getElementById(mother_id);
                if (father != null && mother != null) {
                    var posFather = util.GetTopLeftXY(father);
                    var fwidth = util.GetWidthObject(father);
                    var fheight = util.GetHeightObject(father);
                    if (posFather.length > 0) {
                        lineX1 = posFather[0].x + (fwidth / 2);
                        lineY1 = posFather[0].y + fheight;
                    }

                    var posMother = util.GetTopLeftXY(mother);
                    if (posMother.length > 0) {
                        lineX2 = posMother[0].x;
                        lineY2 = posMother[0].y + fheight / 2;
                    }
                    for (var i = 0; i < lstChildsAndSpouse.length; i++) {
                        var obj = document.getElementById(lstChildsAndSpouse[i].id);
                        if (obj != null) {
                            lstSameLevel.push(obj);
                        }
                    }

                }
                else {
                    if (level == 0) {
                        lstSameLevel = objlst;
                    }
                }
                var objSvg = document.getElementById("svgOne");
                var width = objSvg.width.baseVal.value;
                var height = objSvg.height.baseVal.value;

                var medium = objSvg.width.baseVal.value / 2;
                console.log('medium:' + medium);
                if (father != null && mother != null) {
                    width = (lineX2 - lineX1) * 2.5;
                    medium = (lineX1 + lineX2) / 2;
                }


                if (lstSameLevel.length > 0) {
                    //var beginX = (medium/objlst.length);

                    var distinct = (width) / (lstSameLevel.length + 1);
                    //var beginX = width*0.025+distinct;
                    var beginX = distinct - 25;
                    var widthObj = util.GetWidthObject(obj);
                    if (father != null && mother != null) {
                        //width = (lineX2 - lineX1);
                        //medium = (lineX1+lineX2)/2;
                        distinct = (width) / (lstSameLevel.length + 1);
                        if (distinct < 100) {
                            distinct = 100;
                        }
                        //beginX = distinct;
                        //distinct = 150;
                        var space = (((widthObj) / 2 + ((lstSameLevel.length - 1) * distinct) / 2));
                        beginX = medium - space;
                        //                   console.log("lineX1:"+lineX1);
                        //                   console.log("lineX2:"+lineX2);
                        //                   console.log("medium:"+medium);
                        //                   console.log("space:"+space);
                        //                   console.log("objlst:"+lstSameLevel.length);
                        //                   console.log("widthObj:"+widthObj);
                        //                   console.log("width:"+width);
                        //                   console.log("beginX:"+beginX);
                        //                   console.log("distinct:"+distinct);
                    }
                    // var beginX = width*0.05+distinct;
                    //var distinct = 100;
                    // var widthObj = util.GetWidthObject(obj);
                    //var beginX = medium - (objlst.length*widthObj+objlst.length*distinct-distinct)/2;
                    //var beginX = width*0.05+150;
                    //alert(beginX);
                    for (var i = 0; i < lstSameLevel.length; i++) {

                        if (lstSameLevel[i].constructor.name == "SVGRectElement") {
                            lstSameLevel[i].x.baseVal.value = beginX + (i * distinct);
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objImage.height.baseVal.value / 2);

                            }
                            if (objTxt != null) {
                                objTxt.x.baseVal[0].value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objTxt.textLength.baseVal.value / 2);
                                objTxt.y.baseVal[0].value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objTxt.getBBox().height / 2);
                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);

                                l.x2.baseVal.value = beginX + (i * distinct) + 50;


                            } if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);

                                r.x2.baseVal.value = beginX + (i * distinct) + 50;

                            }
                        }
                        else if (lstSameLevel[i].constructor.name == "SVGCircleElement") {
                            lstSameLevel[i].cx.baseVal.value = beginX + (i * distinct) + 25;
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            if (objTxt != null) {
                                objTxt.x.baseVal[0].value = lstSameLevel[i].cx.baseVal.value - (objTxt.textLength.baseVal.value / 2);;
                                objTxt.y.baseVal[0].value = lstSameLevel[i].cy.baseVal.value - (objTxt.getBBox().height / 2);;
                            }
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = lstSameLevel[i].cx.baseVal.value - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].cy.baseVal.value - (objImage.height.baseVal.value / 2);

                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);
                                l.x2.baseVal.value = beginX + (i * distinct) + 50;
                            }
                            if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);
                                r.x2.baseVal.value = beginX + (i * distinct) + 50;
                            }
                        }
                        else if (lstSameLevel[i].constructor.name == "SVGPolygonElement") {
                            lstSameLevel[i].points[0].x = beginX + (i * distinct);
                            lstSameLevel[i].points[1].x = beginX + (i * distinct) + 25;
                            lstSameLevel[i].points[2].x = beginX + (i * distinct) + 50;
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            if (objTxt != null) {
                                objTxt.x.baseVal[0].value = beginX + (i * distinct) + 25;
                                objTxt.y.baseVal[0].value = lstSameLevel[i].points[1].y;
                            }
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = beginX + (i * distinct) + 25 - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].points[1].y + (objImage.height.baseVal.value / 2);

                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);

                                l.x2.baseVal.value = beginX + (i * distinct) + 50;

                            } if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);

                                r.x2.baseVal.value = beginX + (i * distinct) + 50;

                            }
                        }
                    }
                }
            }
        }

        console.log(objlst);
        //main.nextSibling.ownerSVGElement.childNodes
    }
    this.GetlstChildsAndSpouse = function (father_id, mother_id) {
        var result = [];
        var data = this.GetChildDataById(father_id, mother_id);
        for (var i = 0; i < data.length; i++) {
            result.push(data[i]);
            var tmp = this.FindSpouse(data[i].id, this.spouse);
            if (tmp.length > 0) {
                var p = [];
                if (tmp[0].id1 == data[i].id) {
                    p = this.GetDataById(tmp[0].id2);
                    if (p.length > 0) {
                        result.push(p[0]);
                    }
                }
                else if (tmp[0].id2 == data[i].id) {
                    p = this.GetDataById(tmp[0].id1);
                    if (p.length > 0) {
                        result.push(p[0]);
                    }
                }
            }
        }

        return result;
    }
    this.CreateLineMidium = function () {
        var svg = document.getElementById('svgOne')
        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', svg.width.baseVal.value / 2);
        objLine.setAttributeNS(null, 'y1', svg.y.baseVal.value);
        objLine.setAttributeNS(null, 'x2', svg.width.baseVal.value / 2);
        objLine.setAttributeNS(null, 'y2', svg.y.baseVal.value + svg.height.baseVal.value);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, "id", "line" + name);
        svg.appendChild(objLine);
    }
    this.AddPictureObject = function (idName, x, y) {
        if (this.showPicture == true) {
            var data = this.GetDataById(idName);
            var width = 30;
            var height = 30;
            var objImage = document.createElementNS(svgns, 'image');
            objImage.innerHTML = idName;
            objImage.setAttributeNS(null, 'x', x);
            objImage.setAttributeNS(null, 'y', y);
            if (data.length > 0) {
                if (data[0].picture != null) {
                    objImage.setAttributeNS(null, 'href', data[0].picture);
                    objImage.setAttributeNS(null, "height", height);
                    objImage.setAttributeNS(null, "width", width);
                }
                objImage.setAttributeNS(null, 'id', "image" + idName);
                document.getElementById('svgOne').appendChild(objImage);
                this.AddEvents(objImage);
            }
        }
    }
    this.AddTextObject = function (idName, x, y) {
        var objtxt = document.createElementNS(svgns, 'text');
        objtxt.innerHTML = idName;
        objtxt.setAttributeNS(null, 'x', x);
        objtxt.setAttributeNS(null, 'y', y);
        objtxt.setAttributeNS(null, 'id', "txt" + idName);
        document.getElementById('svgOne').appendChild(objtxt);
    }
    this.AddEvents = function (obj) {
        this.mousedown = false;
        var objX = 0;
        var objY = 0;
        var curX = 0;
        var curY = 0;
        var svg = document.getElementById('svgOne');
        var pt = svg.createSVGPoint();
        if (obj != null) {
            obj.addEventListener("click", function (e) {
                //alert(obj);
            });
            obj.addEventListener("mouseout", function () {
            });
            obj.addEventListener("mousedown", function (e) {
                this.mousedown = true;
                pt.x = e.clientX; pt.y = e.clientY;
                var loc = pt.matrixTransform(svg.getScreenCTM().inverse());
                objX = obj.x.baseVal.value;
                objY = obj.y.baseVal.value;
                curX = loc.x;
                curY = loc.y;
            });
            obj.addEventListener("mouseup", function () {
                this.mousedown = false;
            });
            obj.addEventListener("mouseout", function () {
                this.mousedown = false;
            });
            obj.addEventListener("mousemove", function (e) {
                if (this.mousedown == true) {
                    if (obj.constructor.name == "SVGRectElement") {
                        pt.x = e.clientX; pt.y = e.clientY;
                        var loc = pt.matrixTransform(svg.getScreenCTM().inverse());
                        //obj.x.baseVal.value = loc.x-(obj.width.baseVal.value/2);
                        //obj.y.baseVal.value = loc.y-(obj.height.baseVal.value/2);
                        obj.x.baseVal.value = objX + (loc.x - curX);
                        obj.y.baseVal.value = objY + (loc.y - curY);
                        console.log('mouse x:' + e.x);

                        var objTxt = document.getElementById("txt" + obj.id);
                        var objImage = document.getElementById("image" + obj.id);
                        if (objImage != null) {
                            objImage.x.baseVal.value = obj.x.baseVal.value + (obj.width.baseVal.value / 2) - (objImage.width.baseVal.value / 2);
                            objImage.y.baseVal.value = obj.y.baseVal.value + (obj.height.baseVal.value / 2) - (objImage.height.baseVal.value / 2);

                        }
                        if (objTxt != null) {
                            objTxt.x.baseVal[0].value = obj.x.baseVal.value + (obj.width.baseVal.value / 2) - (objTxt.textLength.baseVal.value / 2);
                            objTxt.y.baseVal[0].value = obj.y.baseVal.value + (obj.height.baseVal.value / 2) - (objTxt.getBBox().height / 2);
                        }

                        var line = document.getElementById("line" + obj.id);
                        var linePC = document.getElementById("linePC" + obj.id);
                        if (line != null) {

                            line.x2.baseVal.value = obj.x.baseVal.value + obj.width.baseVal.value / 2;
                            line.y2.baseVal.value = obj.y.baseVal.value + obj.height.baseVal.value;
                        }
                        if (linePC != null) {

                            linePC.x2.baseVal.value = obj.x.baseVal.value + obj.width.baseVal.value / 2;
                            linePC.y2.baseVal.value = obj.y.baseVal.value;
                        }
                        var l = document.getElementById("l" + obj.id);
                        var r = document.getElementById("r" + obj.id);
                        if (l != null) {
                            l.x1.baseVal.value = obj.x.baseVal.value;
                            l.x2.baseVal.value = obj.x.baseVal.value + obj.width.baseVal.value;
                            l.y1.baseVal.value = obj.y.baseVal.value;
                            l.y2.baseVal.value = obj.y.baseVal.value + obj.height.baseVal.value;
                        }
                        if (r != null) {
                            r.x1.baseVal.value = obj.x.baseVal.value + obj.width.baseVal.value;
                            r.x2.baseVal.value = obj.x.baseVal.value;
                            r.y1.baseVal.value = obj.y.baseVal.value;
                            r.y2.baseVal.value = obj.y.baseVal.value + obj.height.baseVal.value;
                        }

                    }
                }
            });

        }
    }
    this.GetFamilyDetail = function (id, level, index) {
        // Spouses
        //alert(JSON.stringify(this.spouse));
        var main = document.getElementById(id);
        if (main == null || main == undefined) {
            var data = this.GetDataById(id);
            if (data.length > 0) {
                if (data[0].sex == "Male") {
                    this.CMale(id, 100 + (100 * index), (120 * (level + 1)));
                }
                else if (data[0].sex == "Female") {
                    this.CFemale(id, 100 + (100 * index), (120 * (level + 1)));
                }
            }
        }
        var spouselst = this.FindSpouse(id, this.spouse);
        if (spouselst.length > 0) {
            // var spouselst= this.FindSpouse(dataFather[0].id,this.spouse);
            this.CreateSpouse(id, spouselst);
            //this.AdjustPosition(id);

            //var obj = document.getElementById(id);
            //console.log(JSON.stringify(obj));
            //alert(obj);
            // Child

            for (var i = 0; i < spouselst.length; i++) {
                //this.GetFamilyDetail(spouselst[i].id,level+1);
                var dataChilds = this.GetChildDataById(spouselst[i].id1, spouselst[i].id2);
                if (dataChilds.length == 0) {
                    dataChilds = this.GetChildDataById(spouselst[i].id2, spouselst[i].id1);
                }
                if (dataChilds.length > 0) {
                    for (var j = 0; j < dataChilds.length; j++) {
                        this.GetFamilyDetail(dataChilds[j].id, level + 1, j);
                    }
                    this.lstObjLevel.push(dataChilds[0].id);
                }
            }
        }
        else {
            var data = this.GetDataById(id);
            if (data.length > 0) {
                if (data[0].sex == "Male") {
                    this.CMale(id, 100 + (100 * index), 200 + (100 * level));
                }
                else if (data[0].sex == "Female") {
                    this.CFemale(id, 100 + (100 * index), 200 + (100 * level));
                }
            }
        }
    }


    return this;

}
function genogramRelationship(spouse) {
    this.spouse = spouse;
    this.CreateSpouseRelationship = function () {
        for (var i = 0; i < this.spouse.length; i++) {
            this.SpouseRelationship(this.spouse[i].id1, this.spouse[i].id2);
        }
    }
    this.AddLineRelation = function (o) {
        var x1 = o.x1;
        var x2 = o.x2;
        var y1 = o.y1;
        var y2 = o.y2;
        var name = o.name;
        var relation = o.relation;
        var objLine = document.createElementNS(svgns, 'line');
        if (relation == "Marriage") {
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'black');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, "id", "line" + name);
            document.getElementById('svgOne').appendChild(objLine)
        }
        else if (relation == "Engagement") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "10 5 10 5");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine)

        }
        else if (relation == "EngagementAndCohabitation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "10 5 10 5");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine)

        }
        else if (relation == "EngagementAndSeparation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', ((x1 + x2) / 2) - 10);
            objLine.setAttributeNS(null, 'y1', y1 - 10);
            objLine.setAttributeNS(null, 'x2', ((x1 + x2) / 2) + 10);
            objLine.setAttributeNS(null, 'y2', y2 + 10);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "Nullity") {

            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', '#810d00');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM1 = document.createElementNS(svgns, 'line');
            objM1.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 10 - 10);
            objM1.setAttributeNS(null, 'y1', y1 - 10);
            objM1.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 10 - 10);
            objM1.setAttributeNS(null, 'y2', y2 + 10);
            objM1.setAttributeNS(null, 'stroke', '#810d00');
            objM1.setAttributeNS(null, 'stroke-width', 2);
            objM1.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM1);

            var objM2 = document.createElementNS(svgns, 'line');
            objM2.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 10);
            objM2.setAttributeNS(null, 'y1', y1 - 10);
            objM2.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 10);
            objM2.setAttributeNS(null, 'y2', y2 + 10);
            objM2.setAttributeNS(null, 'stroke', '#810d00');
            objM2.setAttributeNS(null, 'stroke-width', 2);
            objM2.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM2);

            var objM3 = document.createElementNS(svgns, 'line');
            objM3.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 10 + 10);
            objM3.setAttributeNS(null, 'y1', y1 - 10);
            objM3.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 10 + 10);
            objM3.setAttributeNS(null, 'y2', y2 + 10);
            objM3.setAttributeNS(null, 'stroke', '#810d00');
            objM3.setAttributeNS(null, 'stroke-width', 2);
            objM3.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM3);

        }
        else if (relation == "LegalCohabitation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "8 3 3 3");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "LegalCohabitationAndSeparationInFact") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "8 3 3 3");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM = document.createElementNS(svgns, 'line');
            objM.setAttributeNS(null, 'x1', ((x1 + x2) / 2) - 10);
            objM.setAttributeNS(null, 'y1', y1 - 10);
            objM.setAttributeNS(null, 'x2', ((x1 + x2) / 2) + 10);
            objM.setAttributeNS(null, 'y2', y2 + 10);
            objM.setAttributeNS(null, 'stroke', 'blue');
            objM.setAttributeNS(null, 'stroke-width', 2);
            objM.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM);

        }
        else if (relation == "LegalCohabitationAndOfficialSeparation") {

            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "8 3 3 3");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM = document.createElementNS(svgns, 'line');
            objM.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 10);
            objM.setAttributeNS(null, 'y1', y1 - 10);
            objM.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 10);
            objM.setAttributeNS(null, 'y2', y2 + 10);
            objM.setAttributeNS(null, 'stroke', 'blue');
            objM.setAttributeNS(null, 'stroke-width', 2);
            objM.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM);

        }
        else if (relation == "CommitedRelationShip") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "5 5 5");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "Cohabitation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "5 2 2 2 2 2");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

        }
        else if (relation == "CohabitationAndSeparation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "5 5 5");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "NonSentimentalCohabitation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 2 2 2 5 2");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

        }
        else if (relation == "NonSentimentalCohabitationAndSeparation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 2 2 2 5 2");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM = document.createElementNS(svgns, 'line');
            objM.setAttributeNS(null, 'x1', ((x1 + x2) / 2) - 10);
            objM.setAttributeNS(null, 'y1', y1 - 10);
            objM.setAttributeNS(null, 'x2', ((x1 + x2) / 2) + 10);
            objM.setAttributeNS(null, 'y2', y2 + 10);
            objM.setAttributeNS(null, 'stroke', 'blue');
            objM.setAttributeNS(null, 'stroke-width', 2);
            objM.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM);
        }
        else if (relation == "CasualRelationshipOrDating") {

            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 2 2");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

        }
        else if (relation == "CasualRelationshipAndSeparation") {

            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 2 2");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "TemporaryRelationOneNightStand") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'blue');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 6 6 6");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "Divorce") {

            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', '#810d00');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM1 = document.createElementNS(svgns, 'line');
            objM1.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 5);
            objM1.setAttributeNS(null, 'y1', y1 - 10);
            objM1.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 15);
            objM1.setAttributeNS(null, 'y2', y2 + 10);
            objM1.setAttributeNS(null, 'stroke', '#810d00');
            objM1.setAttributeNS(null, 'stroke-width', 2);
            objM1.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM1);

            var objM2 = document.createElementNS(svgns, 'line');
            objM2.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 15);
            objM2.setAttributeNS(null, 'y1', y1 - 10);
            objM2.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 5);
            objM2.setAttributeNS(null, 'y2', y2 + 10);
            objM2.setAttributeNS(null, 'stroke', '#810d00');
            objM2.setAttributeNS(null, 'stroke-width', 2);
            objM2.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM2);

        }
        else if (relation == "SeparationInFact") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', '#810d00');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);

            var objM = document.createElementNS(svgns, 'line');
            objM.setAttributeNS(null, 'x1', ((x1 + x2) / 2) - 10);
            objM.setAttributeNS(null, 'y1', y1 - 10);
            objM.setAttributeNS(null, 'x2', ((x1 + x2) / 2) + 10);
            objM.setAttributeNS(null, 'y2', y2 + 10);
            objM.setAttributeNS(null, 'stroke', '#810d00');
            objM.setAttributeNS(null, 'stroke-width', 2);
            objM.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objM);
        }
        else if (relation == "LegalSeparation") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', '#810d00');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
        else if (relation == "LoveAffair") {
            var objLine = document.createElementNS(svgns, 'line');
            objLine.setAttributeNS(null, 'x1', x1);
            objLine.setAttributeNS(null, 'y1', y1);
            objLine.setAttributeNS(null, 'x2', x2);
            objLine.setAttributeNS(null, 'y2', y2);
            objLine.setAttributeNS(null, 'stroke', 'pink');
            objLine.setAttributeNS(null, 'stroke-width', 2);
            objLine.setAttributeNS(null, 'stroke-dasharray', "2 6 6 6");
            objLine.setAttributeNS(null, 'id', 'line' + name);
            document.getElementById('svgOne').appendChild(objLine);
        }
    }
    this.AddLineLeftRight = function (o) {
        //alert(JSON.stringify(o));
        var objLine1 = document.createElementNS(svgns, 'line');
        var l_x1 = o.x1;
        var l_y1 = o.y1;
        var l_x2 = o.x1;
        var l_y2 = o.y1 - o.distinct;
        objLine1.setAttributeNS(null, 'x1', l_x1);
        objLine1.setAttributeNS(null, 'y1', l_y1);
        objLine1.setAttributeNS(null, 'x2', l_x2);
        objLine1.setAttributeNS(null, 'y2', l_y2);
        objLine1.setAttributeNS(null, 'stroke', 'black');
        objLine1.setAttributeNS(null, 'stroke-width', 2);
        objLine1.setAttributeNS(null, "id", "line" + o.name.split('-')[0]);
        document.getElementById('svgOne').appendChild(objLine1);

        var objLine2 = document.createElementNS(svgns, 'line');
        var r_x1 = o.x2;
        var r_y1 = o.y2;
        var r_x2 = o.x2;
        var r_y2 = o.y2 - o.distinct;
        objLine2.setAttributeNS(null, 'x1', r_x1);
        objLine2.setAttributeNS(null, 'y1', r_y1);
        objLine2.setAttributeNS(null, 'x2', r_x2);
        objLine2.setAttributeNS(null, 'y2', r_y2);
        objLine2.setAttributeNS(null, 'stroke', 'black');
        objLine2.setAttributeNS(null, 'stroke-width', 2);
        objLine2.setAttributeNS(null, "id", "line" + o.name.split('-')[1]);
        document.getElementById('svgOne').appendChild(objLine2);
    }
    this.AddHomeObject = function (o) {
        var cx = (o.x1 + o.x2) / 2;
        var cy = o.y1;
        var objHome = document.createElementNS(svgns, 'polygon');
        objHome.setAttributeNS(null, 'cx', cx);
        objHome.setAttributeNS(null, 'cy', cy);
        objHome.setAttributeNS(null, 'points', (cx - 10) + "," + (cy - 5) + " " + (cx) + "," + (cy - 10) + " " + (cx + 10) + "," + (cy - 5) + " " + (cx + 10) + "," + (cy + 5) + " " + (cx - 10) + "," + (cy + 5));
        objHome.setAttributeNS(null, 'fill', '#FFFFFF');
        objHome.setAttributeNS(null, 'stroke', 'blue');
        objHome.setAttributeNS(null, 'stroke-width', 2);
        objHome.setAttributeNS(null, 'id', 'poly' + name);
        document.getElementById('svgOne').appendChild(objHome);
    }
    this.AddSeparationObject = function (o, color) {
        var x1 = o.x1;
        var x2 = o.x2;
        var y1 = o.y1;
        var y2 = o.y2;
        var objM = document.createElementNS(svgns, 'line');
        objM.setAttributeNS(null, 'x1', ((x1 + x2) / 2) - 10);
        objM.setAttributeNS(null, 'y1', y1 - 10);
        objM.setAttributeNS(null, 'x2', ((x1 + x2) / 2) + 10);
        objM.setAttributeNS(null, 'y2', y2 + 10);
        objM.setAttributeNS(null, 'stroke', color);
        objM.setAttributeNS(null, 'stroke-width', 2);
        objM.setAttributeNS(null, 'id', 'line' + name);
        document.getElementById('svgOne').appendChild(objM);
    }
    this.GetDetailObject = function (objId1, objId2) {
        var name = objId1 + "-" + objId2;
        var obj1 = document.getElementById(objId1);
        var obj2 = document.getElementById(objId2);
        var distinct = 25;
        var x1, x2, y1, y2;
        var x1, x2, y1, y2;
        var util = new Utility();
        var data = util.GetPositionXY(obj1);
        x1 = data[0].x;
        y1 = data[0].y;
        var data = util.GetPositionXY(obj2);
        x2 = data[0].x;
        y2 = data[0].y;
        return [
            {
                "x1": x1,
                "y1": y1,
                "x2": x2,
                "y2": y2,
                "distinct": distinct,
                "name": name
            }
        ];
    }
    this.SpouseRelationship = function (objId1, objId2) {
        var obj1 = document.getElementById(objId1);
        var obj2 = document.getElementById(objId2);
        if (obj1 != null && obj2 != null) {
            var result = [];
            for (var i = 0; i < this.spouse.length; i++) {
                if (this.spouse[i].id1 == objId1 && this.spouse[i].id2 == objId2) {
                    result.push(this.spouse[i]);
                }
            }

            //var result = this.spouse.filter(element=>element.id1==objId1 && element.id2==objId2);
            if (result.length > 0) {
                if (result[0].relationStatus == "marriage") {
                    this.Marriage(objId1, objId2);
                }
                else if (result[0].relationStatus == "separation_in_fact") {
                    this.SeparationInFact(objId1, objId2);
                }
                else if (result[0].relationStatus == "legal_separation") {
                    this.LegalSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "divorce") {
                    this.Divorce(objId1, objId2);
                }
                else if (result[0].relationStatus == "engagement") {
                    this.Engagement(objId1, objId2);
                }
                else if (result[0].relationStatus == "engagement_and_cohabitation") {
                    this.EngagementAndCohabitation(objId1, objId2);
                }
                else if (result[0].relationStatus == "engagement_and_separation") {
                    this.EngagementAndSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "nullity") {
                    this.Nullity(objId1, objId2);
                }
                else if (result[0].relationStatus == "legal_cohabitation") {
                    this.LegalCohabitation(objId1, objId2);
                }
                else if (result[0].relationStatus == "legal_cohabitation_and_separation_in_fact") {
                    this.LegalCohabitationAndSeparationInFact(objId1, objId2);
                }
                else if (result[0].relationStatus == "legal_cohabitation_and_official_separation") {
                    this.LegalCohabitationAndOfficialSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "commited_relationship") {
                    this.CommitedRelationShip(objId1, objId2);
                }
                else if (result[0].relationStatus == "cohabitation") {
                    this.Cohabitation(objId1, objId2);
                }
                else if (result[0].relationStatus == "cohabitation_and_separation") {
                    this.CohabitationAndSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "non_sentimental_cohabitation") {
                    this.NonSentimentalCohabitation(objId1, objId2);
                }
                else if (result[0].relationStatus == "non_sentimental_cohabitation_and_separation") {
                    this.NonSentimentalCohabitationAndSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "casual_relationship_or_dating") {
                    this.CasualRelationshipOrDating(objId1, objId2);
                }
                else if (result[0].relationStatus == "casual_relationship_and_separation") {
                    this.CasualRelationshipAndSeparation(objId1, objId2);
                }
                else if (result[0].relationStatus == "temporary_relation_one_night_stand") {
                    this.TemporaryRelationOneNightStand(objId1, objId2);
                }
                else if (result[0].relationStatus == "love_affair") {
                    this.LoveAffair(objId1, objId2);
                }
            }
        }
        //alert(JSON.stringify(result));
    }
    this.Marriage = function (objId1, objId2) {
        var relation = "Marriage";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }

    }
    this.Engagement = function (objId1, objId2) {
        var relation = "Engagement";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.EngagementAndCohabitation = function (objId1, objId2) {
        var relation = "EngagementAndCohabitation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddHomeObject(o);
        }
    }
    this.EngagementAndSeparation = function (objId1, objId2) {

        var relation = "EngagementAndSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.Nullity = function (objId1, objId2) {
        var relation = "Nullity";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.LegalCohabitation = function (objId1, objId2) {
        var relation = "LegalCohabitation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddHomeObject(o);
        }
    }
    this.LegalCohabitationAndSeparationInFact = function (objId1, objId2) {
        var relation = "LegalCohabitationAndSeparationInFact";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.LegalCohabitationAndOfficialSeparation = function (objId1, objId2) {
        var relation = "LegalCohabitationAndOfficialSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.CommitedRelationShip = function (objId1, objId2) {
        var relation = "CommitedRelationShip";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.Cohabitation = function (objId1, objId2) {
        var relation = "Cohabitation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddHomeObject(o);
        }
    }
    this.CohabitationAndSeparation = function (objId1, objId2) {
        var relation = "CohabitationAndSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddSeparationObject(o, "blue");
        }
    }
    this.NonSentimentalCohabitation = function (objId1, objId2) {
        var relation = "NonSentimentalCohabitation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddHomeObject(o);
        }
    }
    this.NonSentimentalCohabitationAndSeparation = function (objId1, objId2) {
        var relation = "NonSentimentalCohabitationAndSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddSeparationObject(o, "blue");
        }
    }
    this.CasualRelationshipOrDating = function (objId1, objId2) {
        var relation = "CasualRelationshipOrDating";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.CasualRelationshipAndSeparation = function (objId1, objId2) {
        var relation = "CasualRelationshipAndSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddSeparationObject(o, "blue");
        }
    }
    this.TemporaryRelationOneNightStand = function (objId1, objId2) {
        var relation = "TemporaryRelationOneNightStand";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.Divorce = function (objId1, objId2) {
        var relation = "Divorce";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.SeparationInFact = function (objId1, objId2) {
        var relation = "SeparationInFact";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    this.LegalSeparation = function (objId1, objId2) {
        var relation = "LegalSeparation";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
            this.AddSeparationObject(o, '#810d00');
        }
    }
    this.LoveAffair = function (objId1, objId2) {
        var relation = "LoveAffair";
        var obj = this.GetDetailObject(objId1, objId2);
        if (obj.length > 0) {
            var o = obj[0];
            o.relation = relation;
            this.AddLineRelation(o);
            this.AddLineLeftRight(o);
        }
    }
    return this;
}
function Utility() {
    this.GetPositionXY = function (obj) {
        var x = 0;
        var y = 0;
        var distinct = 25;
        if (obj != null) {
            if (obj.constructor.name == "SVGRectElement") {
                x = obj.x.baseVal.value + obj.height.baseVal.value / 2;
                y = obj.y.baseVal.value + obj.height.baseVal.value + distinct;
            }
            else if (obj.constructor.name == "SVGCircleElement") {
                x = obj.cx.baseVal.value;
                y = obj.cy.baseVal.value + obj.r.baseVal.value + distinct;
            }
            else {
                x = obj.x.baseVal.value + obj.width.baseVal.value / 2;
                y = obj.y.baseVal.value;
            }
        }
        return [{ "x": x, "y": y }];
    }
    this.GetTopLeftXY = function (obj) {
        var x = 0;
        var y = 0;
        var distinct = 25;
        if (obj != null) {
            if (obj.constructor.name == "SVGRectElement") {
                x = obj.x.baseVal.value;
                y = obj.y.baseVal.value;
            }
            else if (obj.constructor.name == "SVGCircleElement") {
                x = obj.cx.baseVal.value;
                // y = obj.cy.baseVal.value+obj.r.baseVal.value+distinct;
                y = obj.cy.baseVal.value - obj.r.baseVal.value;
            }
            else if (obj.constructor.name == "SVGPolygonElement") {
                x = obj.points[1].x - 25;
                y = obj.points[1].y;
            }
            else {
                x = obj.x.baseVal.value;
                y = obj.y.baseVal.value;
            }
        }
        return [{ "x": x, "y": y }];
    }
    this.GetWidthObject = function (obj) {
        var width = 0;
        if (obj != null) {
            if (obj.constructor.name == "SVGRectElement") {
                width = obj.width.baseVal.value;
            }
            else if (obj.constructor.name == "SVGCircleElement") {
                width = obj.r.baseVal.value * 2;
            }
            else if (obj.constructor.name == "SVGPolygonElement") {
                width = obj.points[2].x - obj.points[0].x;
            }
        }
        return width;
    }
    this.GetHeightObject = function (obj) {
        var height = 0;
        if (obj != null) {
            if (obj.constructor.name == "SVGRectElement") {
                height = obj.height.baseVal.value;
            }
            else if (obj.constructor.name == "SVGCircleElement") {
                height = obj.r.baseVal.value * 2;
            }
            else if (obj.constructor.name == "SVGPolygonElement") {
                height = obj.points[0].x - obj.points[1].x;
            }
        }
        return height;
    }
    return this;
}