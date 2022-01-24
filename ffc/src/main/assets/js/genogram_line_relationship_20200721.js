var svgns = "http://www.w3.org/2000/svg"
function genogramSymbol(data, spouse,twin,idCard) {
    this.data = data;
    this.spouse = spouse;
    this.twin = twin;
    this.lstObjLevel = [];
    this.hisPosition = [];
    this.showPicture = true;
    this.showName = true;
    this.idCard = idCard;
    this.lineColor = '#5a5656';//"#5e5d5d";
    this.minX = 20000;
    this.maxX = -20000;
    this.genderMain = "Male";
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
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'stroke-width', '2');
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', '#000000');
        // obj.setAttributeNS(null, 'stroke-width', 2);
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', '#000000');
        // obj.setAttributeNS(null, 'stroke-width', 2);
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', '#000000');
        // obj.setAttributeNS(null, 'stroke-width', 2);
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
        document.getElementById('svgOne').appendChild(obj);

        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddPictureObject(idName, x, y);

        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', y);
        objLine.setAttributeNS(null, 'x2', x + width);
        objLine.setAttributeNS(null, 'y2', y + height);
        objLine.setAttributeNS(null, 'stroke', this.lineColor);
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
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', '#000000');
        // obj.setAttributeNS(null, 'stroke-width', 2);
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'stroke-width', '2');
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        // obj.setAttributeNS(null, 'stroke-width', '2');
        // obj.setAttributeNS(null, 'fill', '#FFFFFF');
        // obj.setAttributeNS(null, 'stroke', 'rgb(0,0,0)');
        // obj.setAttributeNS(null, 'id', idName);
        this.SetObjectProperty(idName,obj);
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
        var path_question = "images/question.png"
        var objGeno = document.getElementById(idName);
        
        var obj = document.createElementNS(svgns, 'rect');
        var width = 50;
        var height = 50;
        obj.setAttributeNS(null, 'x', x);
        obj.setAttributeNS(null, 'y', y);
        obj.setAttributeNS(null, 'height', height);
        obj.setAttributeNS(null, 'width', width);
        obj.setAttributeNS(null, 'id', idName);
        // obj.setAttributeNS(null, 'stroke-width', '2');
        obj.setAttributeNS(null, 'fill', '#f5f5f5');
        obj.setAttributeNS(null, 'stroke', '#f5f5f5');
        // obj.setAttributeNS(null, 'id', idName);
        //this.SetObjectProperty(idName,obj);
        document.getElementById('svgOne').appendChild(obj);
            // var data = this.GetDataById(idName);
        // var width = 50;
        // var height = 50;
        // var obj = document.createElementNS(svgns, 'image');
        // obj.innerHTML = idName;
        // obj.setAttributeNS(null, 'x', x);
        // obj.setAttributeNS(null, 'y', y);
        // obj.setAttributeNS(null, 'href', path_question);
        // obj.setAttributeNS(null, "height", height);
        // obj.setAttributeNS(null, "width", width);
        // obj.setAttributeNS(null, 'id', idName);
        // document.getElementById('svgOne').appendChild(obj);
                //objGeno.setAttributeNS(null, "fill", "url("+path_person+")");
                //this.AddEvents(objImage);
         
        this.AddEvents(obj);
        this.AddTextObject(idName, x, y);
        this.AddQuestionObject(idName,x,y);
        // this.AddPictureObject(idName, x, y);
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
            if ((
                 (this.data[i]["father_id"] === father_id && this.data[i]["mother_id"] === mother_id)
                 || 
                 (this.data[i]["mother_id"] === father_id && this.data[i]["father_id"] === mother_id)
                 )) {
                result.push(this.data[i]);
            }
        }
        return result.sort(compare);
        //return result;
    }
    function compare( a, b ) {
        if ( a.age > b.age ){
          return -1;
        }
        if ( a.age < b.age ){
          return 1;
        }
        return 0;
    }
    this.FindSpouse = function (id, spouse) {
        var slist = spouse;
        var result = [];
        for (var i = 0; i < slist.length; i++) {
            if (slist[i].id1 == id /*|| slist[i].id2 == id*/) {
                result.push(slist[i]);
            }
        }
        var data = [];
        var isDupplicate = false;
        for (var i = 0; i < result.length; i++) {
            isDupplicate = false;
            for(var j=0;j<data.length;j++){
                if(result[i].id1==data[j].id1 && result[i].id2==data[j].id2){
                    isDupplicate =true;
                    break;
                }
            }
            if(!isDupplicate){
                data.push(result[i]);
            }
        }
        return data;
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
                        else {
                 
                            this.GenderUnknown(p[0].id,200,300);
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
                     
                            this.CFemale(p[0].id, x + 150 * (i + 1), y);
                        }
                        else {
              
                            this.GenderUnknown(p[0].id,x + 150 * (i + 1), y);
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
                // var father = document.getElementById(father_id);
                // var mother = document.getElementById(mother_id);
                var lineObj = document.getElementById("line" + father_id + "-" + mother_id);
                if (lineObj == null) {
                    lineObj = document.getElementById("line" + mother_id + "-" + father_id);
                }
                var x1 = 0, y1 = 0, x2 = 0, y2 = 0;
                if (lineObj != null) {

                    var lstChild = this.GetChildDataById(father_id, mother_id);
                    var seqNo = this.GetSeqOfSpouse(father_id,mother_id);
                    if(seqNo>1)
                    {
                        x1 = lineObj.x1.baseVal.value+((lineObj.x2.baseVal.value - lineObj.x1.baseVal.value)*(seqNo-1))/(seqNo);
                    }
                    else{
                        x1 = lineObj.x1.baseVal.value;
                    }
                    
                    

                    //var step = (lineObj.x2.baseVal.value - lineObj.x1.baseVal.value) / (lstChild.length + 1);
                    var step = (lineObj.x2.baseVal.value - x1) / (lstChild.length + 1);
                    y1 = lineObj.y1.baseVal.value;
                    //this.CreateVertical(x1);
                    var util = new Utility();
                    var xStart = 0;
                    var xEnd = 0;
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
                            //this.CreateVertical(x1 + ((1 + i) * step));
                            var x0=0;
                            if(step>=0){
                                //x0= x1 + ((1 + i) * step)
                                x0= x2;
                            } else {
                                //x0= x1 + ((1 + (lstChild.length-1-i)) * step)
                                x0= x2;
                            }
                            if(i==0){
                                xStart = x2;
                            }
                            else if(i==lstChild.length-1){
                                xEnd = x2;
                            }
                            objLine.setAttributeNS(null, 'x1', x0);
                            objLine.setAttributeNS(null, 'y1', (y1+y2)/2);
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

                            

                            var  xlinkns = "http://www.w3.org/1999/xlink";
                            var useObject =document.createElementNS(svgns,'use');
                            useObject.setAttributeNS(null, 'id', "recTxtAgeuse"+lstChild[i].id);
                            useObject.setAttributeNS(xlinkns, 'href',"#recTxtAge"+lstChild[i].id);
                            document.getElementById('svgOne').appendChild(useObject);

                            var  xlinkns = "http://www.w3.org/1999/xlink";
                            var useObject =document.createElementNS(svgns,'use');
                            useObject.setAttributeNS(null, 'id', "useAge"+lstChild[i].id);
                            useObject.setAttributeNS(xlinkns, 'href',"#txtAge"+lstChild[i].id);
                            document.getElementById('svgOne').appendChild(useObject);
                        }
                    }
                    
                    var objLine = document.createElementNS(svgns, 'line');
                    objLine.setAttributeNS(null, 'x1', (x1+lineObj.x2.baseVal.value)/2);
                    objLine.setAttributeNS(null, 'y1', lineObj.y1.baseVal.value);
                    objLine.setAttributeNS(null, 'x2', (x1+lineObj.x2.baseVal.value)/2);
                    objLine.setAttributeNS(null, 'y2', (lineObj.y1.baseVal.value+y2)/2);
                    objLine.setAttributeNS(null, 'stroke', 'black');
                    objLine.setAttributeNS(null, 'stroke-width', 2);
                    objLine.setAttributeNS(null, "id", "lineFMV" + name);
                    document.getElementById('svgOne').appendChild(objLine);


                    var objLine = document.createElementNS(svgns, 'line');
                    if(xEnd==0){
                        xEnd=xStart;
                    }
                    debugger;
                    objLine.setAttributeNS(null, 'x1', xStart);
                    objLine.setAttributeNS(null, 'y1', (y1+y2)/2);
                    objLine.setAttributeNS(null, 'x2', xEnd);
                    objLine.setAttributeNS(null, 'y2', (y1+y2)/2);
                    objLine.setAttributeNS(null, 'stroke', 'black');
                    objLine.setAttributeNS(null, 'stroke-width', 2);
                    objLine.setAttributeNS(null, "id", "lineRelation" + name);
                    document.getElementById('svgOne').appendChild(objLine);
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
        //main.nextSibling.ownerSVGElement.childNodes
        //this.CreateLineMidium();
        var level = 0;
        var result = this.GetDataById(id);

        if(result.length>0) {
            var dataFather = this.GetDataById(result[0].father_id);
            var dataMother = this.GetDataById(result[0].mother_id);
            if (dataFather != null || this.dataMother!=null) {
                if(dataFather.length>0 || dataMother.length>0){
                    var dataParent = [];
                    if(dataFather.length>0)
                    {
                        this.CMale(dataFather[0].id, 200, 200);
                        dataParent = dataFather;
                        this.genderMain = "Male";
                    }
                    else if(dataMother.length>0){
                        this.Female(dataMother[0].id, 200, 200);
                        dataParent = dataMother;
                        this.genderMain = "Female";
                    }
                    //this.CMale(dataFather[0].id, 200, 200);
                    var util = new Utility();
                    var objFather = document.getElementById(dataParent[0].id);
                    var pos = util.GetTopLeftXY(objFather) 
                    
                    this.lstObjLevel.push({"id":dataParent[0].id,"y":pos[0].y});
                    this.GetFamilyDetail(dataParent[0].id, level + 1,1);
                
                    //this.genderMain = "Male";
                    this.lstObjLevelIntSort = this.lstObjLevel.sort(function (a, b) { return parseInt(a.y) - parseInt(b.y) });
                    for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
                        this.AdjustPosition(this.lstObjLevelIntSort[i].id, level);
                        level = level + 1;
                    }
                
                    var rel = new genogramRelationship(this.spouse,this.twin,this.hisPosition);
                    rel.CreateSpouseRelationship();
                    for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
                        this.CreateRelationship(this.lstObjLevelIntSort[i].id)
                    }
                    rel.CreateTwinRelationship();
                }
                else{
                    if(result[0].sex === "Male"){
                    this.genderMain = "Male";
                    this.CMale(result[0].id, 200, 200);
                    }
                    else if(result[0].sex === "Female"){
                        this.genderMain = "Female";
                        this.CFemale(result[0].id, 200, 200);
                    }
                    var util = new Utility();
                    var objFather = document.getElementById(result[0].id);
                    var pos = util.GetTopLeftXY(objFather) 
                    
                    this.lstObjLevel.push({"id":result[0].id,"y":pos[0].y});
                    this.GetFamilyDetail(result[0].id, level + 1,1);
                
                    this.lstObjLevelIntSort = this.lstObjLevel.sort(function (a, b) { return parseInt(a.y) - parseInt(b.y) });
                    for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
               
                        this.AdjustPosition(this.lstObjLevelIntSort[i].id, level);
                        level = level + 1;
                    }
                    var rel = new genogramRelationship(this.spouse,this.twin,this.hisPosition);
                    rel.CreateSpouseRelationship();
                    for (var i = 0; i < this.lstObjLevelIntSort.length; i++) {
                        this.CreateRelationship(this.lstObjLevelIntSort[i].id)
                    }
                    rel.CreateTwinRelationship();
                    }
                // alert(this.getWidthContent());
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
               
                // if (dataChilds.length == 0) {
                //     dataChilds = this.GetChildDataById(spouselst[i].id2, spouselst[i].id1);
                // }
                if (dataChilds.length > 0) {
                    for (var j = 0; j < dataChilds.length; j++) {
                        this.GetFamilyDetail(dataChilds[j].id, level + 1, (j+1));
                    }
                    var util = new Utility();
                    var objChild = document.getElementById(dataChilds[0].id);
                    var pos = util.GetTopLeftXY(objChild);
                    this.lstObjLevel.push({"id":dataChilds[0].id,"y":pos[0].y});
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
                        //x2 = data[0].x;
                        y2 = data[0].y;
                    }
                } else if (lst[i].constructor.name == "SVGCircleElement") {
                    var data = util.GetTopLeftXY(lst[i]);
                    if (data.length > 0) {
                        //x2 = data[0].x;
                        y2 = lst[i].cy.baseVal.value - lst[i].r.baseVal.value;
                    }
                } else if (lst[i].constructor.name == "SVGPolygonElement") {
                    var data = util.GetTopLeftXY(lst[i]);
                    if (data.length > 0) {
                        //x2 = data[0].x;
                        y2 = data[0].y;
                    }
                // } else if (lst[i].constructor.name == "SVGImageElement") {

                //     var data = util.GetTopLeftXY(lst[i]);
                //     if (data.length > 0) {
                //         //x2 = data[0].x;
                //         y2 = data[0].y;
                //     }
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
            var tmp =[];
            for(var i=0;i<lstParent.length;i++){
                var isHave=false;
                for(var j=0;j<tmp.length;j++){
                    if(lstParent[i].data==tmp[j].data){
                        isHave=true;
                        break;
                    }
                }
                if(!isHave){
                    tmp.push(lstParent[i]);
                }
            }
            //tmp = lstParent;
            var lstSameLevel = [];
            var lastPositionX = 0;
            console.log(JSON.stringify(tmp));
            for (var j = 0; j < tmp.length; j++) {
                lstSameLevel = [];
                var lineX1 = 0, lineX2 = 0, lineY1 = 0, lineY2 = 0;
                var father_id = 0, mother_id = 0;
                father_id = tmp[j].data.split(',')[0];
                mother_id = tmp[j].data.split(',')[1];
                var seqNo = this.GetSeqOfSpouse(father_id,mother_id);
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
                else if(father != null && mother == null)
                {
                    var posFather = util.GetTopLeftXY(father);
                    var fwidth = util.GetWidthObject(father);
                    var fheight = util.GetHeightObject(father);
                    if (posFather.length > 0) {
                        lineX1 = posFather[0].x + (fwidth / 2);
                        lineY1 = posFather[0].y + fheight;
                        lineX2 = posFather[0].x + (fwidth / 2);
                        lineY2 = posFather[0].y + fheight;
                    }

                    for (var i = 0; i < lstChildsAndSpouse.length; i++) {
                        var obj = document.getElementById(lstChildsAndSpouse[i].id);
                        if (obj != null) {
                            lstSameLevel.push(obj);
                        }
                    }
                }
                else if(father == null && mother != null)
                {
                    var posMother = util.GetTopLeftXY(mother);
                    var fwidth = util.GetWidthObject(mother);
                    var fheight = util.GetHeightObject(mother);
                    if (posMother.length > 0) {
                        lineX1 = posMother[0].x;
                        lineY1 = posMother[0].y + fheight / 2;
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
                // var medium = (width)*0.9/2;
                var medium = objSvg.width.baseVal.value / 2;
                //console.log('medium:'+medium);

                if ((father != null && mother != null)
                     ||(father != null && mother == null)
                     ||(father == null && mother != null)
                    ) {
                    width = (lineX2 - lineX1) * 1;
                    medium = (lineX1 + lineX2) / 2;
                    if(seqNo>1)
                    {
                        lineX1 = medium;
                        width =  (lineX2-lineX1)*1;
                        medium = (lineX1 + lineX2) /2;
                    }
                }
                console.log('medium:' + medium);

                if (lstSameLevel.length > 0) {
                    //var beginX = (medium/objlst.length);
                    var distinct = (width) / (lstSameLevel.length + 1);
                    var beginX = distinct - 25;
                    // if(level==0){
                    //      distinct = (width) / (lstSameLevel.length + 1)*2;
                    //      beginX = width*0.1;
                    // }
                     
                    
                    //var beginX = width*0.1;
                    var widthObj = util.GetWidthObject(obj);
                    if ((father != null && mother != null) 
                    || (father != null && mother == null)
                    || (father == null && mother != null)
                    ) {
                        //width = (lineX2 - lineX1);
                        //medium = (lineX1+lineX2)/2;
                        distinct = (width) / (lstSameLevel.length + 1);
                        if (distinct < 100) {
                            distinct = 100;
                        }
                        //beginX = distinct;
                        //distinct = 150;
                        var space = (((widthObj) / 2 + ((lstSameLevel.length - 1) * distinct) / 2));
                        beginX = (medium - space);


                        // console.log("lineX1:"+lineX1);
                        // console.log("lineX2:"+lineX2);
                        // console.log("medium:"+medium);
                        // console.log("space:"+space);
                        // console.log("objlst:"+lstSameLevel.length);
                        // console.log("widthObj:"+widthObj);
                        // console.log("width:"+width);
                        // console.log("beginX:"+beginX);
                        // console.log("distinct:"+distinct);
                    }
                    
                    for (var i = 0; i < lstSameLevel.length; i++) {
                        if (lstSameLevel[i].constructor.name == "SVGRectElement") {
                            lastPositionX = beginX + (i * distinct);
                            lstSameLevel[i].x.baseVal.value = beginX + (i * distinct);
                            var n = 0;
                            var max = beginX;
                            while (this.isOverlap(level,lstSameLevel[i].x.baseVal.value) && n<this.hisPosition.length )
                            {
                                lstSameLevel[i].x.baseVal.value = beginX + ((n) * distinct);
                                n++;
                                // max=this.GetMaxXPosition(level,this.hisPosition);
                                // lstSameLevel[i].x.baseVal.value = max + distinct;
                            }
                            // if(this.isOverlap(level,lstSameLevel[i].x.baseVal.value )){
                            //     lstSameLevel[i].x.baseVal.value = beginX + ((i+1) * distinct);
                            //     if(this.isOverlap(level,lstSameLevel[i].x.baseVal.value))
                            //     {
                            //         lstSameLevel[i].x.baseVal.value = beginX + ((i+2) * distinct);
                            //     }
                            //     this.hisPosition.push({level:level, id:lstSameLevel[i].id, x:lstSameLevel[i].x.baseVal.value});
                            // }
                            // else {
                                this.hisPosition.push({level:level, id:lstSameLevel[i].id, x:lstSameLevel[i].x.baseVal.value});
                            // }
                            this.keepWidthMinMax(lstSameLevel[i].x.baseVal.value);
                            console.log("i:" + i + ", id:" + lstSameLevel[i].id + ", x:" + lstSameLevel[i].x.baseVal.value);
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            var objTxtAge = document.getElementById("txtAge" + lstSameLevel[i].id);
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            var objRecText = document.getElementById("recTxt"+lstSameLevel[i].id);
                            var objRecTextAge = document.getElementById("recTxtAge"+lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objImage.height.baseVal.value / 2);

                            }
                            if (objTxt != null) {
                                if(this.showName)
                                {
                                    objTxt.x.baseVal[0].value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objTxt.getBBox().width / 2);
                                    objTxt.y.baseVal[0].value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value) + (objTxt.getBBox().height);
                                }
                                // else 
                                // {
                                //     objTxt.x.baseVal[0].value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objTxt.textLength.baseVal.value / 2) -20;
                                //     objTxt.y.baseVal[0].value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objTxt.getBBox().height / 2) -20;
                                // }
                                // objTxt.y.baseVal[0].value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objTxt.getBBox().height / 2) -20;
                            }
                            if(objRecText!=null){
                                objRecText.x.baseVal.value = objTxt.x.baseVal[0].value;
                                objRecText.y.baseVal.value = objTxt.y.baseVal[0].value-12;
                            }
                            if(objTxtAge !=null){
                                objTxtAge.x.baseVal[0].value = lstSameLevel[i].x.baseVal.value + (lstSameLevel[i].width.baseVal.value / 2) - (objTxtAge.textLength.baseVal.value / 2) -20;
                                objTxtAge.y.baseVal[0].value = lstSameLevel[i].y.baseVal.value + (lstSameLevel[i].height.baseVal.value / 2) - (objTxtAge.getBBox().height / 2) -20;
                            }
                            if(objRecTextAge!=null){
                                objRecTextAge.x.baseVal.value = objTxtAge.x.baseVal[0].value;
                                objRecTextAge.y.baseVal.value = objTxtAge.y.baseVal[0].value-13;
                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);
                                l.x2.baseVal.value = beginX + (i * distinct) + 50;
                                // l.x1.baseVal.value = max + distinct;
                                // l.x2.baseVal.value = max + distinct + 50;
                            } if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);
                                r.x2.baseVal.value = beginX + (i * distinct) + 50;

                                // r.x1.baseVal.value = max + distinct;
                                // r.x2.baseVal.value = max + distinct + 50;
                            }
                        }
                        else if (lstSameLevel[i].constructor.name == "SVGCircleElement") {
                            lastPositionX = beginX + (i * distinct) + 25;
                            lstSameLevel[i].cx.baseVal.value = beginX + (i * distinct) + 25;
                            var n = 0;
                            var max = beginX;
                            while (this.isOverlap(level,lstSameLevel[i].cx.baseVal.value) && n<this.hisPosition.length)
                            {
                               lstSameLevel[i].cx.baseVal.value = beginX + ((n) * distinct) + 25;
                               n++;
                            //    max=this.GetMaxXPosition(level,this.hisPosition);
                            //    lstSameLevel[i].cx.baseVal.value = max + distinct+25;
                            } 
                            this.hisPosition.push({level:level, id:lstSameLevel[i].id, x:lstSameLevel[i].cx.baseVal.value});
                            this.keepWidthMinMax(lstSameLevel[i].cx.baseVal.value);
                            console.log("i:" + i + ", id:" + lstSameLevel[i].id + ", x:" + lstSameLevel[i].cx.baseVal.value);
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            var objTxtAge = document.getElementById("txtAge" + lstSameLevel[i].id);
                            var objRecText = document.getElementById("recTxt"+lstSameLevel[i].id);
                            var objRecTextAge = document.getElementById("recTxtAge"+lstSameLevel[i].id);
                            if (objTxt != null) {
                                if(this.showName)
                                {
                                    objTxt.x.baseVal[0].value = lstSameLevel[i].cx.baseVal.value - (objTxt.getBBox().width/ 2);
                                    objTxt.y.baseVal[0].value = lstSameLevel[i].cy.baseVal.value+lstSameLevel[i].r.baseVal.value + (objTxt.getBBox().height);
                                }
                                else
                                {
                                    objTxt.x.baseVal[0].value = lstSameLevel[i].cx.baseVal.value - (objTxt.textLength.baseVal.value / 2)-20;
                                    objTxt.y.baseVal[0].value = lstSameLevel[i].cy.baseVal.value - (objTxt.getBBox().height / 2) -20;
                            
                                }
                            }
                            if (objTxtAge != null) {
                                objTxtAge.x.baseVal[0].value = lstSameLevel[i].cx.baseVal.value - (objTxtAge.textLength.baseVal.value / 2)-20;
                                objTxtAge.y.baseVal[0].value = lstSameLevel[i].cy.baseVal.value - (objTxtAge.getBBox().height / 2) -20;
                            }
                            if(objRecTextAge!=null){
                                objRecTextAge.x.baseVal.value = objTxtAge.x.baseVal[0].value;
                                objRecTextAge.y.baseVal.value = objTxtAge.y.baseVal[0].value-13;
                            }
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = lstSameLevel[i].cx.baseVal.value - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].cy.baseVal.value - (objImage.height.baseVal.value / 2);

                            }
                            if(objRecText!=null){
                                objRecText.x.baseVal.value = objTxt.x.baseVal[0].value;
                                objRecText.y.baseVal.value = objTxt.y.baseVal[0].value-12;
                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);
                                l.x2.baseVal.value = beginX + (i * distinct) + 50;

                                // l.x1.baseVal.value = max + distinct;
                                // l.x2.baseVal.value = max + distinct + 50;
                            }
                            if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);
                                r.x2.baseVal.value = beginX + (i * distinct) + 50;

                                // r.x1.baseVal.value = max + distinct;
                                // r.x2.baseVal.value = max + distinct + 50;
                            }
                        }
                        else if (lstSameLevel[i].constructor.name == "SVGPolygonElement") {
                            lastPositionX = beginX + (i * distinct);
                            lstSameLevel[i].points[0].x = beginX + (i * distinct);
                            lstSameLevel[i].points[1].x = beginX + (i * distinct) + 25;
                            lstSameLevel[i].points[2].x = beginX + (i * distinct) + 50;
                            var n = 0;
                            var max = beginX;
                            while (this.isOverlap(level,lstSameLevel[i].points[1].x) && n<this.hisPosition.length)
                            {
                                lstSameLevel[i].points[0].x = beginX + ((i+n) * distinct);
                                lstSameLevel[i].points[1].x = beginX + ((i+n) * distinct) + 25;
                                lstSameLevel[i].points[2].x = beginX + ((i+n) * distinct) + 50;
                                n++;

                            //     n++;
                            //     max=this.GetMaxXPosition(level,this.hisPosition);
                              
                            //    lstSameLevel[i].points[0].x = max + distinct;
                            //    lstSameLevel[i].points[1].x = max + distinct + 25;
                            //    lstSameLevel[i].points[2].x = max + distinct + 50;
                            } 
                            this.hisPosition.push({level:level, id:lstSameLevel[i].id, x:lstSameLevel[i].points[1].x});
                            
                            this.keepWidthMinMax(lstSameLevel[i].points[0].x);
                            console.log("i:" + i + ", id:" + lstSameLevel[i].id + ", x:" + lstSameLevel[i].points[0].x);
                            var l = document.getElementById("l" + lstSameLevel[i].id);
                            var r = document.getElementById("r" + lstSameLevel[i].id);
                            var objTxt = document.getElementById("txt" + lstSameLevel[i].id);
                            var objTxtAge = document.getElementById("txtAge" + lstSameLevel[i].id);
                            var objRecText = document.getElementById("recTxt"+lstSameLevel[i].id);
                            if (objTxt != null) {
                                if(this.showName){
                                    objTxt.x.baseVal[0].value = beginX + (i * distinct) + 25 - 20;
                                    objTxt.y.baseVal[0].value = lstSameLevel[i].points[1].y + objTxt.getBBox().height+ 50;
                                }
                                else {
                                    objTxt.x.baseVal[0].value = beginX + (i * distinct) + 25 - 20;
                                    objTxt.y.baseVal[0].value = lstSameLevel[i].points[1].y - 20 ;
                                }
                            }
                            if (objTxtAge != null) {
                                objTxtAge.x.baseVal[0].value = beginX + (i * distinct) + 25 - 20;
                                objTxtAge.y.baseVal[0].value = lstSameLevel[i].points[1].y - 20 ;
                            }
                            var objImage = document.getElementById("image" + lstSameLevel[i].id);
                            if (objImage != null) {
                                objImage.x.baseVal.value = beginX + (i * distinct) + 25 - (objImage.width.baseVal.value / 2);
                                objImage.y.baseVal.value = lstSameLevel[i].points[1].y + (objImage.height.baseVal.value / 2);

                            }
                            if(objRecText!=null){
                                objRecText.x.baseVal.value = objTxt.x.baseVal[0].value;
                                objRecText.y.baseVal.value = objTxt.y.baseVal[0].value-12;
                            }
                            if (l != null) {
                                l.x1.baseVal.value = beginX + (i * distinct);

                                l.x2.baseVal.value = beginX + (i * distinct) + 50;

                                // l.x1.baseVal.value = max + distinct;

                                // l.x2.baseVal.value = max + distinct+ 50;

                            } if (r != null) {
                                r.x1.baseVal.value = beginX + (i * distinct);

                                r.x2.baseVal.value = beginX + (i * distinct) + 50;

                                // r.x1.baseVal.value = max + distinct;

                                // r.x2.baseVal.value = max + distinct + 50;

                            }
                        } 
                        // else if (lstSameLevel[i].constructor.name == "SVGImageElement")
                        // {
                        //     lastPositionX = beginX + (i * distinct);
                        //     lstSameLevel[i].x.baseVal.value = beginX + (i * distinct);
                        //     debugger;
                        // }
                    }

                    console.log('lastPositionX:' + lastPositionX);
                }
            }
        }
        this.SetColorPersonFocus();
        console.log(objlst);
        //main.nextSibling.ownerSVGElement.childNodes
    }
   
    this.GetMaxXPosition = function (level,data){
        var max=0;
        for(var i =0;i<data.length;i++){
            if(data[i].level==level && max < data[i].x){
                max= data[i].x;
            }
        }
        return max;
    }
    this.GetlstChildsAndSpouse = function (father_id, mother_id) {
        var result = [];
        var data = this.GetChildDataById(father_id, mother_id);
        for (var i = 0; i < data.length; i++) {
            var isNotDup = true;
            for(var k = 0;k<result.length;k++)
            {
                if(result[k].id == data[i].id )
                {
                    isNotDup=false;
                    break;
                }
            }
            if(isNotDup){
                result.push(data[i]);
            }
            //result.push(data[i]);
            var tmp = this.FindSpouse(data[i].id, this.spouse);
            if (tmp.length > 0) {
                var p = [];
                for(var j = 0;j<tmp.length; j++){
                    if (tmp[j].id1 == data[i].id) {
                        p = this.GetDataById(tmp[j].id2);
                        if (p.length > 0) {
                            var isNotDup = true;
                            for(var k = 0;k<result.length;k++)
                            {
                                if(result[k].id == p[0].id )
                                {
                                    isNotDup=false;
                                    break;
                                }
                            }
                            if(isNotDup){
                                result.push(p[0]);
                            }
                        }
                    }
                    else if (tmp[j].id2 == data[i].id) {
                        p = this.GetDataById(tmp[j].id1);
                        if (p.length > 0) {
                            var isNotDup = true;
                            for(var k = 0;k<result.length;k++)
                            {
                                if(result[k].id == p[0].id )
                                {
                                    isNotDup=false;
                                    break;
                                }
                            }
                            if(isNotDup){
                                result.push(p[0]);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    this.CreateVertical = function (x) {

        var svg = document.getElementById('svgOne')
        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x);
        objLine.setAttributeNS(null, 'y1', 0);
        objLine.setAttributeNS(null, 'x2', x);
        objLine.setAttributeNS(null, 'y2', svg.height.baseVal.value);
        objLine.setAttributeNS(null, 'stroke', 'red');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, "id", "CV" + x);
        svg.appendChild(objLine);
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
    this.AddQuestionObject = function (idName, x, y) {
        var path_question = "images/question.png"
        var width = 40;
        var height = 40;
        var objImage = document.createElementNS(svgns, 'image');
        objImage.innerHTML = idName;
        objImage.setAttributeNS(null, 'x', x);
        objImage.setAttributeNS(null, 'y', y);
        objImage.setAttributeNS(null, 'href',path_question);
        objImage.setAttributeNS(null, "height", height);
        objImage.setAttributeNS(null, "width", width);
        objImage.setAttributeNS(null, 'id', "image" + idName);
        document.getElementById('svgOne').appendChild(objImage);
        this.AddEvents(objImage);
    }
    this.AddPictureObject = function (idName, x, y) {
        var path_person = "images/person.png"
        var objGeno = document.getElementById(idName);
        if (this.showPicture == true) {
            var data = this.GetDataById(idName);
            var width = 40;
            var height = 40;
            var objImage = document.createElementNS(svgns, 'image');
            objImage.innerHTML = idName;
            objImage.setAttributeNS(null, 'x', x);
            objImage.setAttributeNS(null, 'y', y);
            if (data.length > 0) {
                if (data[0].picture != null) {

                    console.log('picture path1:'+data[0].picture);
                    if(data[0].picture!="")
                    {
                        console.log('picture path2:'+data[0].picture);
                        path_person = data[0].picture
                        objImage.setAttributeNS(null, 'href', data[0].picture);
                    }
                    else{
                        objImage.setAttributeNS(null, 'href',path_person);
                    }
                }
                else{
                    objImage.setAttributeNS(null, 'href',path_person);
                }
                objImage.setAttributeNS(null, "height", height);
                objImage.setAttributeNS(null, "width", width);
                objImage.setAttributeNS(null, 'id', "image" + idName);
                document.getElementById('svgOne').appendChild(objImage);
                //objGeno.setAttributeNS(null, "fill", "url("+path_person+")");
                this.AddEvents(objImage);
            }
        }
    }
    this.AddTextObject = function (idName, x, y) {
        this.AddTextNameObject(idName,x,y);
        this.AddTextAgeObject(idName,x,y);
    }
    this.AddTextNameObject = function(idName,x,y){
        var objtxt = document.createElementNS(svgns, 'text');
        var data = this.GetDataById(idName);
        var info = "";
        if(this.showName){
            info = data[0].first_name; //+" "+data[0].last_name;
        }
        objtxt.innerHTML = info;
        objtxt.setAttributeNS(null, 'x', x);
        objtxt.setAttributeNS(null, 'y', y);
        objtxt.setAttributeNS(null, 'id', "txt" + idName);
        objtxt.setAttributeNS(null, 'fill', "#5a5656");
        //objtxt.setAttributeNS(null,'fill','url(#solid)')
        document.getElementById('svgOne').appendChild(objtxt);
        var objtxt= document.getElementById("txt" + idName)
        SVGRect = objtxt.getBBox();
        var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", SVGRect.x);
        rect.setAttribute("y", SVGRect.y);
        rect.setAttribute("id", "recTxt"+idName);
        rect.setAttribute("width", SVGRect.width);
        rect.setAttribute("height", SVGRect.height);
        rect.setAttribute("fill", "#f5f5f5");
        document.getElementById('svgOne').insertBefore(rect, objtxt);
        

    }
    this.AddTextAgeObject = function (idName, x, y) {
        var objtxt = document.createElementNS(svgns, 'text');
        var data = this.GetDataById(idName);
        var age =  data[0].age;
        objtxt.innerHTML = age;
        objtxt.setAttributeNS(null, 'x', x);
        objtxt.setAttributeNS(null, 'y', y);
        objtxt.setAttributeNS(null, 'id', "txtAge" + idName);
        objtxt.setAttributeNS(null, 'fill', "#5a5656");
        document.getElementById('svgOne').appendChild(objtxt);

        var objtxt= document.getElementById("txtAge" + idName)
        SVGRect = objtxt.getBBox();
        var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", SVGRect.x);
        rect.setAttribute("y", SVGRect.y);
        rect.setAttribute("id", "recTxtAge"+idName);
        rect.setAttribute("width", SVGRect.width);
        rect.setAttribute("height", SVGRect.height);
        rect.setAttribute("fill", "#f5f5f5");
        //rect.setAttribute("fill", "yellow");
        document.getElementById('svgOne').insertBefore(rect, objtxt);

        // var  xlinkns = "http://www.w3.org/1999/xlink";
        // var useObject =document.createElementNS(svgns,'use');
        // useObject.setAttributeNS(null, 'id', "recTxtAgeuse"+idName);
        // useObject.setAttributeNS(xlinkns, 'href',"#recTxtAge"+idName);
        // document.getElementById('svgOne').appendChild(useObject);

        // var  xlinkns = "http://www.w3.org/1999/xlink";
        // var useObject =document.createElementNS(svgns,'use');
        // useObject.setAttributeNS(null, 'id', "txtAgeuse"+idName);
        // useObject.setAttributeNS(xlinkns, 'href',"#txtAge"+idName);
        // document.getElementById('svgOne').appendChild(useObject);

        

    }
    this.AddEvents = function (obj) {
        this.mousedown = false;
        this.touchobj = false;
        var id = obj.id.replace("image","");
        obj.personData = this.GetDataById(id);
        var objX = 0;
        var objY = 0;
        var curX = 0;
        var curY = 0;
        var svg = document.getElementById('svgOne');
        var pt = svg.createSVGPoint();
        if (obj != null) {
            obj.addEventListener("click", function (e) {
                var msg = "";
                if(obj.personData.length>0){
                    //alert("id:"+obj.personData[0].id+" "+obj.personData[0].first_name+" "+obj.personData[0].last_name+" age:"+obj.personData[0].age+" sex:"+obj.personData[0].sex);
                    msg = "id:"+obj.personData[0].id+" "+obj.personData[0].first_name+" "+obj.personData[0].last_name+" age:"+obj.personData[0].age+" sex:"+obj.personData[0].sex;
                }
               // Android.showToast(msg);
                Android.showPersonalInfo(obj.personData[0].id,obj.personData[0].personId);
            });
            obj.addEventListener("mouseout", function () {
            });
            obj.addEventListener("mousedown", function (e) {
                // this.mousedown = true;
                // pt.x = e.clientX; pt.y = e.clientY;
                // var loc = pt.matrixTransform(svg.getScreenCTM().inverse());
                // objX = obj.x.baseVal.value;
                // objY = obj.y.baseVal.value;
                // curX = loc.x;
                // curY = loc.y;
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
            obj.addEventListener("touchstart", function (e) {
                this.touchobj = true;
                 //alert(obj);
            },false);
            obj.addEventListener("touchmove", function (e) {
                console.log('touchmove');
                if (this.touchobj == true) {
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
            },false);
            obj.addEventListener("touchenter", function (e) {
                console.log('touchenter');
            },false);
        }
    }
    this.GetFamilyDetail = function (id, level, index) {
        // Spouses
        debugger;
        //alert(JSON.stringify(this.spouse));
        var main = document.getElementById(id);
        if (main == null || main == undefined) {
            var data = this.GetDataById(id);
            if (data.length > 0) {
                if (data[0].sex == "Male") {
                    this.CMale(id, 100 + (100 * index), 20*(level-1) + (120* (level+1)));
                }
                else if (data[0].sex == "Female") {
                    this.CFemale(id, 100 + (100 * index), 20*(level-1) +(120 * (level+1)));
                }
                else if (data[0].sex == "Unknow") {
                    this.GenderUnknown(id, 100 + (100 * index), 20*(level-1) +(120 * (level+1)));
                }
            }
        }
        var spouselst = this.FindSpouse(id, this.spouse);
        if (spouselst.length > 0) {
            this.CreateSpouse(id, spouselst);
            for (var i = 0; i < spouselst.length; i++) {
        
                var dataChilds = this.GetChildDataById(spouselst[i].id1, spouselst[i].id2);
                // if (dataChilds.length == 0) {
                //     dataChilds = this.GetChildDataById(spouselst[i].id2, spouselst[i].id1);
                // }
                if (dataChilds.length > 0) {
                    for (var j = 0; j < dataChilds.length; j++) {
                        this.GetFamilyDetail(dataChilds[j].id, level + 1, (j+1));
                        
                    }
                    var util = new Utility();
                    var objChlid = document.getElementById(dataChilds[0].id);
                    var pos = util.GetTopLeftXY(objChlid) 
                    this.lstObjLevel.push({"id":dataChilds[0].id,"y":pos[0].y});
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
    this.GetSeqOfSpouse = function(id1,id2){
        var seqNo=0;
        if(this.genderMain === "Male"){
            for(var i = 0;i<this.spouse.length;i++){
                if(this.spouse[i].id1==id1 && this.spouse[i].id2==id2){
                    seqNo = this.spouse[i].seq;
                    break;
                }
            }
        }
        else if(this.genderMain === "Female") {
            for(var i = 0;i<this.spouse.length;i++){
            if(this.spouse[i].id1==id2 && this.spouse[i].id2==id1){
                seqNo = this.spouse[i].seq;
                break;
            }
            }
        }
        return seqNo;
    }
    this.SetObjectProperty = function(idName,obj){
        obj.setAttributeNS(null, 'stroke-width', '1');
        obj.setAttributeNS(null, 'fill', '#fcf9f9');
        obj.setAttributeNS(null, 'stroke', this.lineColor); //#969393
        obj.setAttributeNS(null, 'style', 'filter:url(#dropshadow)');
        obj.setAttributeNS(null, 'id', idName);
    }
    this.SetColorPersonFocus = function(){
        var data = this.GetDataById(this.idCard);
        var obj = document.getElementById(this.idCard);
        obj.setAttributeNS(null, 'fill', '#b2f8df');
        
    }
    this.keepWidthMinMax = function(x){
        if(this.minX>x) {
            this.minX = x;
        }
        if(this.maxX<x){
            this.maxX = x;
        }
    }
    this.getWidthContent = function(){
        var widthContent = this.maxX - this.minX;
        return widthContent;
    }
    this.getFirstPosition = function(){
        return this.minX;
    }
    this.isOverlap = function(level,x){
        
        var isOverlap = false;
        if(level!==0){
            for(var i = 0;i <this.hisPosition.length; i++){
                if( level == this.hisPosition[i].level 
                    && (
                        (x-25<=(this.hisPosition[i].x+25))
                        //  || (x<=this.hisPosition[i].x+100)
                    )
                )
                {
                    isOverlap=true;
                    break;
                }
            }
        }
        return isOverlap;
    }
    return this;

}
function genogramRelationship(spouse,twin,hisPosition) {
    this.spouse = spouse;
    this.twin = twin;
    this.hisPosition = hisPosition;
    this.getCutDupSpouse = function(){
        var helper = {};
        var sp = this.spouse.reduce((r,o)=>{

            var key1 = o.id1 + '-' + o.id2;
            var key2 = o.id2 + '-' + o.id1;
            if(!helper[key2]) {
              helper[key1] = Object.assign({}, o); // create a copy of o
              r.push(helper[key1]);
            }
            // if(!helper[key2]) {
            //     helper[key2] = Object.assign({}, o); // create a copy of o
            //     r.push(helper[key2]);
            //   }
            return r;
          }, []);
        return sp;
    }
    this.CreateSpouseRelationship = function () {
        var mySpouse = this.spouse;
        //var mySpouse = this.getCutDupSpouse();
        var tmpIds = [];
        for (var i = 0; i < mySpouse.length; i++) {
            // if(tmpIds.indexOf(mySpouse[i].id1+"-"+mySpouse[i].id2)<0){
            //     tmpIds.push(mySpouse[i].id1+"-"+mySpouse[i].id2);
            //     tmpIds.push(mySpouse[i].id2+"-"+mySpouse[i].id1);
                this.SpouseRelationship(mySpouse[i].id1, mySpouse[i].id2);
            // }
            //this.SpouseRelationship(mySpouse[i].id1, mySpouse[i].id2);
        }
    }
    this.CreateTwinRelationship = function(){
        for(var i =0;i<this.twin.length;i++){
            var ids = this.twin[i].ids.split(",");
            var type = this.twin[i].type;
            if(ids!=null){
                var twinObj = [];
                var twinlinePCObj = [];
                var util = new Utility();
                for(var j =0;j<ids.length;j++){
                    var obj = document.getElementById(ids[j]);
                    if(obj!=null){
                        twinObj.push({
                            "id":ids[j],
                            "x":util.GetTopLeftXY(obj)[0].x,
                        });
                    }

                    var objlinePC = document.getElementById("linePC"+ids[j]);
                    if(objlinePC!=null){
                        twinlinePCObj.push(objlinePC);
                    }
                }
                var result = twinObj.sort(util.GetSortOrder("x"));
                var minX2=100000000,maxX2=-100000000;
                for(var k=0;k<twinlinePCObj.length;k++){
                    if(minX2>twinlinePCObj[k].x2.baseVal.value)
                    {
                        minX2 = twinlinePCObj[k].x2.baseVal.value;
                    }
                    if(maxX2<twinlinePCObj[k].x2.baseVal.value)
                    {
                        maxX2 = twinlinePCObj[k].x2.baseVal.value;
                    }
                }
                var center = (minX2+maxX2)/2;
                for(var k=0;k<twinlinePCObj.length;k++){
                    twinlinePCObj[k].x1.baseVal.value = center;
                }
  
                if(type=="identical_twins")
                if(result.length>1){
                    for(var l=0;l<result.length-1;l++){
                        this.CreateLineTwinIdentiCal(result[l].id,result[l+1].id);
                    }
                }
            }
        }
    }
    this.CreateLineTwinIdentiCal=function(id1,id2){

        var obj1 = document.getElementById(id1);
        var obj2 = document.getElementById(id2);
        var x1,x2,y1,y2;
        var util = new Utility();
        if(obj1!=null){
            var pos =util.GetTopLeftXY(obj1)[0];
            var size = util.GetSizeObject(obj1)[0];
            if (obj1.constructor.name == "SVGRectElement") {
                x1 = pos.x;
            }
            else if (obj1.constructor.name == "SVGCircleElement") {
                x1 = pos.x+obj1.r.baseVal.value;
            }
            y1 = pos.y+size.height/2;
        }

        if(obj2!=null){
            var pos =util.GetTopLeftXY(obj2)[0];
            var size = util.GetSizeObject(obj2)[0];
            if (obj2.constructor.name == "SVGRectElement") {
                x2 = pos.x;
            }
            else if (obj1.constructor.name == "SVGCircleElement") {
                x2 = pos.x-obj2.r.baseVal.value;
            }
            y2 = pos.y+size.height/2;
        }
        var objLine = document.createElementNS(svgns, 'line');
        objLine.setAttributeNS(null, 'x1', x1);
        objLine.setAttributeNS(null, 'y1', y1);
        objLine.setAttributeNS(null, 'x2', x2);
        objLine.setAttributeNS(null, 'y2', y2);
        objLine.setAttributeNS(null, 'stroke', 'black');
        objLine.setAttributeNS(null, 'stroke-width', 2);
        objLine.setAttributeNS(null, "id", "linetwin" + id1+"-"+id2);
        document.getElementById('svgOne').appendChild(objLine)

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
            var x0 = 0;
            var isMoreRelation = false;
            var objX1 = this.getXHisPositionById(o.id1);
            var objX2 = this.getXHisPositionById(o.id2);
            if(objX1!=null && objX2!=null){
                if(objX1.index<objX2.index)
                {
                    if(objX2.index-objX1.index>1){
                        isMoreRelation = true;
                        x0 = this.hisPosition[objX2.index-1].x;
                    }
                }
                else if(objX1.index>objX2.index)
                {
                    if(objX1.index-objX2.index>1){
                        isMoreRelation = true;
                        x0 = this.hisPosition[objX1.index+1].x;
                    }
                }
            }
            var arrName = name.split("-");
            var switchName = arrName[1]+"-"+arrName[0];
            var tmpObjLine = document.getElementById('line'+switchName);
            if(tmpObjLine==null){
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
            if(isMoreRelation){
                x1=x0;
            }
            var tmpObjM = document.getElementById('lineM1'+switchName);
            if(tmpObjM==null){
                var objM1 = document.createElementNS(svgns, 'line');
                objM1.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 5);
                objM1.setAttributeNS(null, 'y1', y1 - 10);
                objM1.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 15);
                objM1.setAttributeNS(null, 'y2', y2 + 10);
                objM1.setAttributeNS(null, 'stroke', '#810d00');
                objM1.setAttributeNS(null, 'stroke-width', 2);
                objM1.setAttributeNS(null, 'id', 'lineM1' + name);
                document.getElementById('svgOne').appendChild(objM1);

                var objM2 = document.createElementNS(svgns, 'line');
                objM2.setAttributeNS(null, 'x1', ((x1 + x2) / 2) + 15);
                objM2.setAttributeNS(null, 'y1', y1 - 10);
                objM2.setAttributeNS(null, 'x2', ((x1 + x2) / 2) - 5);
                objM2.setAttributeNS(null, 'y2', y2 + 10);
                objM2.setAttributeNS(null, 'stroke', '#810d00');
                objM2.setAttributeNS(null, 'stroke-width', 2);
                objM2.setAttributeNS(null, 'id', 'lineM2' + name);
                document.getElementById('svgOne').appendChild(objM2);
            }
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

        var  xlinkns = "http://www.w3.org/1999/xlink";
        var useObject =document.createElementNS(svgns,'use');
        useObject.setAttributeNS(null, 'id', "recTxtuse"+o.name.split('-')[0]);
        useObject.setAttributeNS(xlinkns, 'href',"#recTxt"+o.name.split('-')[0]);
        document.getElementById('svgOne').appendChild(useObject);

        var  xlinkns = "http://www.w3.org/1999/xlink";
        var useObject =document.createElementNS(svgns,'use');
        useObject.setAttributeNS(null, 'id', "txtuse"+o.name.split('-')[0]);
        useObject.setAttributeNS(xlinkns, 'href',"#txt"+o.name.split('-')[0]);
        document.getElementById('svgOne').appendChild(useObject);

       

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

        var  xlinkns = "http://www.w3.org/1999/xlink";
        var useObject =document.createElementNS(svgns,'use');
        useObject.setAttributeNS(null, 'id', "recTxtuse"+o.name.split('-')[1]);
        useObject.setAttributeNS(xlinkns, 'href',"#recTxt"+o.name.split('-')[1]);
        document.getElementById('svgOne').appendChild(useObject);

        var  xlinkns = "http://www.w3.org/1999/xlink";
        var useObject =document.createElementNS(svgns,'use');
        useObject.setAttributeNS(null, 'id', "use"+o.name.split('-')[1]);
        useObject.setAttributeNS(xlinkns, 'href',"#txt"+o.name.split('-')[1]);
        document.getElementById('svgOne').appendChild(useObject);
        
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
        if(obj1!=null && obj2==null){
            obj2=obj1;
        }
        else if(obj1==null && obj2!=null){
            obj1=obj2;
        }
        var distinct = 30;
        var x1, x2, y1, y2;
        var x1, x2, y1, y2;
        var util = new Utility();
        var data = util.GetPositionXY(obj1);
        x1 = data[0].x;
        y1 = data[0].y+5;
        var data = util.GetPositionXY(obj2);
        x2 = data[0].x;
        y2 = data[0].y+5;
        return [
            {
                "id1": objId1,
                "id2": objId2,
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
        if ((obj1 != null && obj2 != null)
          ||(obj1 != null && obj2 == null)
          ||(obj1 == null && obj2 != null)
          ) {
            var result = [];
            var sp = this.spouse;// this.getCutDupSpouse();
            for (var i = 0; i < sp.length; i++) {
                if (sp[i].id1 == objId1 && sp[i].id2 == objId2) {
                    result.push(sp[i]);
                    break;
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
    this.getXHisPositionById = function (id){
        var x = 0;
        var index = -1;
        for(var i = 0; i< this.hisPosition.length; i++){
            if(this.hisPosition[i].id==id){
                x = this.hisPosition[i].x;
                index = i;
            }
        }
        return {x:x,index:index};
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
            else if (obj.constructor.name == "SVGImageElement") {
                x = obj.getBBox().x + obj.getBBox().width / 2;
                y = obj.getBBox().y + obj.getBBox().height*1.5;
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
            else if (obj.constructor.name == "SVGImageElement") {
                x = obj.getBBox().x;
                y = obj.getBBox().y;
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
            else if (obj.constructor.name == "SVGImageElement") {
                width = obj.getBBox().width;
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
            else if (obj.constructor.name == "SVGImageElement") {
                height = obj.getBBox().height;
            }
        }
        return height;
    }
    this.GetSizeObject = function(obj){
        var result = [];
        var height = 0;
        var width = 0;
        if(obj!=null){
            height = this.GetHeightObject(obj);
            width = this.GetWidthObject(obj);
        }
        result = [{
            "width":width,
            "height":height
        }];
        return result;
    }
    this.GetSortOrder = function(prop) {
        return function(a, b) {
            if (a[prop] > b[prop]) {
                return 1;
            } else if (a[prop] < b[prop]) {
                return -1;
            }
            return 0;
        }
    }
    return this;
}
function TransformData(sourceGenogram){
    this.sourceGenogram = sourceGenogram;
    this.data = [];
    this.spouse = [];
    this.twin = [];
    this.Transform = function()
    {
        for(var i = 0;i<this.sourceGenogram.length;i++){
 
            if(this.sourceGenogram[i]!=null)
            {   
                var members =  this.sourceGenogram[i];
                // for(var j = 0;j<members.length;j++)
                // {
                  
                    var obj = {};
                    obj.personId=members.id;
                    obj.id = members.id.toString();
                    var objPersonInfo = this.getPersonInfo(obj.id);
                    obj.first_name = objPersonInfo.first_name;
                    obj.last_name = objPersonInfo.last_name;
                    obj.sex = objPersonInfo.sex;
                    obj.birthday = objPersonInfo.birthday;
                    obj.age = objPersonInfo.age;
                    obj.father_id=objPersonInfo.father_id;
                    obj.mother_id=objPersonInfo.mother_id;
                    if(objPersonInfo.spouse.length>0)
                    {
                        for(var j = 0;j<objPersonInfo.spouse.length;j++){
                            var sp = {};
                            sp.id1 = obj.id;
                            sp.id2 = objPersonInfo.spouse[j];
                            sp.seq = (j+1);
                            sp.relationStatus = this.getRelationStatus(sp.id1,sp.id2);
                            var objPersonInfo1 = this.getPersonInfo(sp.id1);
                            var objPersonInfo2 = this.getPersonInfo(sp.id2);

                            sp.sex1 = objPersonInfo1.sex;
                            sp.sex2 = objPersonInfo2.sex;
                            sp.relationships1 = objPersonInfo1.relationships;
                            sp.relationships2 = objPersonInfo2.relationships;
                            this.spouse.push(sp);

                            var sp = {};
                            sp.id1 = objPersonInfo.spouse[j];
                            sp.id2 = obj.id; 
                            sp.seq = (j+1);

                            sp.relationStatus=this.getRelationStatus(sp.id1,sp.id2);
                            var objPersonInfo1 = this.getPersonInfo(sp.id1);
                            var objPersonInfo2 = this.getPersonInfo(sp.id2);
                            sp.sex1 = objPersonInfo1.sex;
                            sp.sex2 = objPersonInfo2.sex;
                            sp.relationships1 = objPersonInfo1.relationships;
                            sp.relationships2 = objPersonInfo2.relationships;
                           
                            this.spouse.push(sp);
                        }
                    }
                    if(obj.father_id!=null && obj.mother_id !=null)
                    {
                        if(obj.father_id!="" && obj.mother_id!="")
                        {
                          
                            var sp = {};
                            // get father id
                            
                            sp.id1=obj.father_id;
                            sp.id2=obj.mother_id; 
                    
                            sp.relationStatus=this.getRelationStatus(sp.id1,sp.id2);
                            var objPersonInfo1 = this.getPersonInfo(sp.id1);
                            var objPersonInfo2 = this.getPersonInfo(sp.id2);
                          
                            sp.sex1 = objPersonInfo1.sex;
                            sp.sex2 = objPersonInfo2.sex;
                            sp.relationships1 = objPersonInfo1.relationships;
                            sp.relationships2 = objPersonInfo2.relationships;

                            this.spouse.push(sp);
                            var sp = {};
                            sp.id1=obj.mother_id;
                            sp.id2=obj.father_id; 
                            // sp.id1= this.getSystemIdByDataRelationShips(members.relationships,"Mother");
                            // sp.id2= this.getSystemIdByDataRelationShips(members.relationships,"Father");
                            sp.relationStatus=this.getRelationStatus(sp.id1,sp.id2);
                            var objPersonInfo1 = this.getPersonInfo(sp.id1);
                            var objPersonInfo2 = this.getPersonInfo(sp.id2);
                            sp.sex1 = objPersonInfo1.sex;
                            sp.sex2 = objPersonInfo2.sex;
                            sp.relationships1 = objPersonInfo1.relationships;
                            sp.relationships2 = objPersonInfo2.relationships;
                           
                            this.spouse.push(sp);
                        }
                    }
                    obj.status=objPersonInfo.status;
                    obj.picture=objPersonInfo.picture;
                    obj.relationships = members.relationships;
                    this.data.push(obj);
                    if(objPersonInfo.spouse!=null){
                        var lstSP = objPersonInfo.spouse;
                        for(var k = 0;k<lstSP.length;k++){
                           
                            var sp = {};
                            if(obj.id!="" && lstSP[k].toString()!=""){
                               
                                sp.id1=obj.id
                                //sp.id2= this.getIdCardBySysId(lstSP[k].toString()); 
                                sp.id2= lstSP[k].toString(); 
                                sp.relationStatus="marriage";

                                var objPersonInfo1 = this.getPersonInfo(sp.id1);
                                var objPersonInfo2 = this.getPersonInfo(sp.id2);
                                sp.sex1 = objPersonInfo1.sex;
                                sp.sex2 = objPersonInfo2.sex;
                                sp.relationships1 = objPersonInfo1.relationships;
                                sp.relationships2 = objPersonInfo2.relationships;
                                
                                this.spouse.push(sp);
                            }
                        }
                    }
                    if(members.relationships!=null){
                        // if(members.relationships!=null){
                            var lstTwin = [];
                            for(var l=0;l <members.relationships.length;l++){
                                if(members.relationships[l].relate!=null){
                                    if(members.relationships[l].relate.toUpperCase() === 'TWIN'){
                                        var id = members.relationships[l].id;
                                        lstTwin.push(id);
                                        //lstTwin.push(members[j].properties.relationships[l].id);
                                    }
                                    else if(members.relationships[l].relate.toUpperCase() === 'SIBLING'){
                                        //var person = this.GetDataById(members[j].properties.relationships[l].id);
                                        var id1 = members.id;
                                        var id2 = members.relationships[l].id;
                                        var person1 = this.getPersonInfo(id1);
                                        var person2 = this.getPersonInfo(id2);
                                  
                                        if(person1.birthday === person2.birthday){
                                          //lstTwin.push(members[j].properties.relationships[l].id);
                                          lstTwin.push(id2);
                                        }
                                       
                                    }
                                }
                            }
                            var strLstTwin = "";
                            if(lstTwin.length>0){
                                //strLstTwin = members[j].properties.id+','+lstTwin.join(',');
                                strLstTwin = members.id+','+lstTwin.join(',');
                            }
                            var objTwin ={};
                            if(strLstTwin.trim()!==""){
                                objTwin.ids= strLstTwin;
                                objTwin.type = "twins";
                                this.twin.push(objTwin);
                              
                            }
                        // }
                    }
                // }
            }
        }
        this.spouse =  this.getCutDupSpouse();
        for(var i = 0;i<this.spouse.length-1;i++){
            var icount = 1;
            if(this.spouse[i].seq==null){
                this.spouse[i].seq = icount;
            }
            for(var j= i+1;j<this.spouse.length;j++){
                 
                if(this.spouse[i].id1 == this.spouse[j].id1 )
                {
                    if(this.spouse[j].seq==null){
                        icount=icount+1;
                        this.spouse[j].seq = icount;
                    }
                }
            }
        }
        for(var i = 0;i<this.spouse.length-1;i++){
            var icount = 1;
            // if(this.spouse[i].seq==null){
            //     this.spouse[i].seq = icount;
            // }
            for(var j= i+1;j<this.spouse.length;j++){
                 
                if(this.spouse[i].id2 == this.spouse[j].id2 )
                {
                    // if(this.spouse[j].seq==null){
                        icount=icount+1;
                        this.spouse[j].seq = icount;
                    // }
                }
            }
        }
        console.log("======== data ========")
        console.log(this.data);
        console.log("======== spouse ========")
        console.log(this.spouse);
        console.log("======== twin ========")
        console.log(this.twin);
        return [{"data":this.data,"spouse":this.spouse,"twin":this.twin}];;
    }
    this.getCutDupSpouse = function(){
        var helper = {};
        var sp = this.spouse.reduce((r,o)=>{
            var key1 = o.id1 + '-' + o.id2;
            if(!helper[key1]) {
              helper[key1] = Object.assign({}, o); // create a copy of o
              r.push(helper[key1]);
            }
            return r;
          }, []);
        return sp;
    }
    this.getSystemIdByDataRelationShips = function (relationships,relate){
        var id="0";
        for(var i = 0;i<relationships.length;i++){
            var data = relationships[i];
            if(data.relate==relate){
                id= data.id;
                break;
            }
        }
        return id;
    }
    this.getPersonInfo = function(id){
        var obj = {};
        
        //console.log("this.sourceGenogram.members:"+this.sourceGenogram[0].members);
        if(this.sourceGenogram!=null){
            for(var i = 0 ; i<this.sourceGenogram.length;i++){
                var data = this.sourceGenogram[i];
               // alert(JSON.stringify(data));
                // console.log(data);
                // console.log('data:'+data.idCard);
                if(data.id == id){
                   
                    obj.first_name = data.firstname;
                    obj.last_name = data.lastname;
                    if(data.sex.toUpperCase()=="MALE")
                    {
                        obj.sex = "Male";
                    }
                    else if(data.sex.toUpperCase()=="FEMALE"){
                        obj.sex = "Female";
                    }
                    else 
                    {
                        obj.sex = "Unknow";
                    }
                    obj.father_id=this.getSystemIdByDataRelationShips(data.relationships,"Father");
                    obj.mother_id=this.getSystemIdByDataRelationShips(data.relationships,"Mother");
                    if(data.death == null){
                        obj.status="live";
                    }
                    else if(data.death!=null){
                        obj.status="dead";
                    }
                    var spouse =[];
                    /*  "Married"
                        "Seperated"
                        "LegallySeperated" 
                        "Divorced"
                        "Engaged"
                        "LoveAffair" */
                    var spouseStatus = ["MARRIED","DIVORCED","SEPERATED","LEGALLYSEPERATED","ENGAGED","LOVEAFFAIR"];
                    for(var z=0;z<data.relationships.length;z++){
                        if(spouseStatus.indexOf(data.relationships[z].relate.toUpperCase())>-1)
                          {
                           spouse.push(data.relationships[z].id)
                        }
                    }
                    obj.spouse = spouse;
                    // if(data.husband!=null){
                    //     obj.spouse = data.husband;
                    // }else if(data.wife!=null){
                    //     obj.spouse = data.wife;
                    // }
                    if(data.relationships!=null){
                        obj.birthday = data.birthDate;
                        obj.age = this.getAge(obj.birthday);
                        obj.relationships = data.relationships;
                    }
                    //alert(data.avatarUrl);
                    // if(data.properties!=null){

                        var url = data.avatarUrl;
                        if(url!=null){
                            url = url.replace("images/","images%2f").replace("profile/","profile%2f");
                            obj.picture = url;
                        }
                        //alert(obj.picture);
                    // }
                    
                    break;
                }
            }
        }
        return obj;
    }
    this.getAge = function(birthday){
        var age="";
        if(birthday!=undefined){
            var d = birthday.split("-");
            var date = d[2];
            var month = d[1];
            var year = d[0];
            const date1 = new Date(year,month,date);
            const date2 = Date.now();
            const diffTime = Math.abs(date2 - date1);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
            age = Math.floor(diffDays/365);
            console.log(age);
        }
        return age.toString();
    }
    this.getIdCardBySysId = function(id){
       
        var idCard = "0";
        if(this.sourceGenogram!=null){
            for(var i = 0 ; i<this.sourceGenogram.length;i++){
                if(this.sourceGenogram[i].id==id)
                {
                    
                    idCard = this.sourceGenogram[i].identities[0].id;
                    break;
                }
            }
        }
        return idCard;
    }
    this.getSysIdByIdCard = function(idCard){
       
        var id = "0";
        if(this.sourceGenogram!=null){
            for(var i = 0 ; i<this.sourceGenogram.length;i++){
                if(this.sourceGenogram[i].identities[0].id==idCard)
                {
                    id = this.sourceGenogram[i].id;
                    break;
                }
            }
        }
        return id;
    }
    this.getRelationStatus = function(id1,id2){
        var relate = "marriage";
        //var id = this.getSysIdByIdCard(id2);
        var id = id2;
        if(this.sourceGenogram!=null)
        {
            var source = this.sourceGenogram;
            for(var i = 0 ; i<source.length;i++){
                if(source[i].id==id1)
                {
                    for(var j = 0;j<source[i].relationships.length;j++){
                        if(source[i].relationships[j].id==id){
                            relate = source[i].relationships[j].relate;
                        }
                    }
                    // if(source.members[i].linkedStack!=null)
                    // {
                    //     for(var j = 0;j<source.members[i].linkedStack.length;j++){
                    //         if(source.members[i].linkedStack[j]==id2)
                    //         {
                    //             relate = source.members[i].properties.relationships[j].relate;
                    //             break;
                    //         }
                    //     }
                    // }
                    // break;
                }
            }
        }
        var result = relate;
        switch(result){
            case "Married":
                result="marriage";
                break;
            case "Seperated":
                result="separation_in_fact";
                break;
            case "LegallySeperated":
                result="legal_separation";
                break; 
            case "Divorced":
                result="divorce";
                break;
            case "Engaged":
                result="engagement";
                break;
            case "LoveAffair":
                result="love_affair";
                break;
            default:
                result="marriage";
        }
        return result;
    }
    return this;
}
function ValidateData(data,spouse,twin,idCard){
    this.data = data;
    this.spouse = spouse;
    this.twin = twin;
    this.error = [];
    this.isValid = function(){
        var result = true;
        var b1 = this.isSpouseAbNormal(data,spouse);
        var b2 = this.isFatherOrMotherMoreThanOne(data);
        var b3 = this.isWrongMemberOfFamilyFatherMother(data,spouse);
        var b4 = this.isNotHaveData(idCard);

        return !(b1 || b2 || b3 || b4) ;
        //return result;
    }
    this.isFatherOrMotherMoreThanOne = function(data)
    {
        var result = false;
        var iCountFather = 0;
        var iCountMother = 0;
        var lstFathers = [];
        var lstMothers = [];
        var person = "";
        var prefix = "";
        for(var i = 0;i<data.length;i++){
            iCountFather = 0;
            iCountMother = 0;
            lstFathers = [];
            lstMothers = [];
            person = this.GetDataById(data[i].id)[0];
            prefix = "คุณ";
            for(var j = 0;j< data[i].relationships.length;j++){
                var name = "ไม่ระบุชื่อในระบบ";
                var relate = data[i].relationships[j].relate;
                if(data[i].relationships[j].name!=""){
                    name = data[i].relationships[j].name;
                }
                if(relate.toUpperCase() == "FATHER" && data[i].relationships[j].name!=""){
                    lstFathers.push(name);
                    iCountFather = iCountFather+1;
                }
                if(relate.toUpperCase() == "MOTHER" && data[i].relationships[j].name!=""){
                    lstMothers.push(name);
                    iCountMother = iCountMother+1;
                }
            }
           
            if(iCountFather>1 ){
                this.error.push(prefix+" "+person.first_name+" "+person.last_name);
                this.error.push("\nมี พ่อมากกว่า 1 คน");
                this.error.push("\n - "+lstFathers.join("\n - "));
                result = true;
                //alert("isFatherOrMotherMoreThanOne");
                break;
            }
            if(iCountMother>1 ){
                this.error.push(prefix+" "+person.first_name+" "+person.last_name);
                this.error.push("\nมี แม่มากกว่า 1 คน");
                this.error.push("\n - "+lstMothers.join("\n - "));
                 result = true;
                 //alert("isFatherOrMotherMoreThanOne");
                 break;
            }
        }
        return result;
    }
    this.isSpouseAbNormal = function(data,spouse){
        var result = false;
        for(var i = 0; i<spouse.length;i++){
          if(spouse[i].id1 !=0 && spouse[i].id2!=0){
            if(spouse[i].sex1==spouse[i].sex2){
                var person1 = this.GetDataById(spouse[i].id1);
                var person2 = this.GetDataById(spouse[i].id2);
                if(person1.length>0 && person2.length>0){
                    var message = [];
                    var prefix = "";
                    if(person1[0].sex.toUpperCase()=="MALE"){
                        prefix = "นาย";
                    }else if(person1[0].sex.toUpperCase()=="FEMALE"){
                        prefix = "นางสาว";
                    }
                    message.push("\n\""+prefix+" "+person1[0].first_name+" "+person1[0].last_name+"\"");
                    message.push(" แต่งงานกับ ");
                    message.push("\""+prefix+" "+person2[0].first_name+" "+person2[0].last_name+"\"");
                    this.error.push(message.join(" "));
                }
                 
                result = true;
                //alert("isSpouseAbNormal");
                break;
            }
          }
          
        }
        return result;
    }
    this.isWrongMemberOfFamilyFatherMother = function(data,spouse)
    {
      var result = false;
      for(var i = 0 ; i<spouse.length; i++)
        {
            var married1 = 0;
            var married2 = 0;
            var childs1 = 0;
            var childs2 = 0;
            var person1 = this.GetDataById(spouse[i].id1)[0];
            var person2 = this.GetDataById(spouse[i].id2)[0];
            if(spouse[i].relationships1!=null && spouse[i].relationships2 !=null)
            {
                for(var k = 0;k < spouse[i].relationships1.length;k++)
                {
                    if(spouse[i].relationships1[k].relate.toUpperCase() == "MARRIED"){
                        married1 = married1 +1;
                    }
                }
                for(var k = 0;k < spouse[i].relationships2.length;k++)
                {
                    if(spouse[i].relationships2[k].relate.toUpperCase() == "MARRIED"){
                        married2 = married2 +1;
                    }
                }
                for(var k = 0;k < spouse[i].relationships1.length;k++)
                {
                    if(spouse[i].relationships1[k].relate.toUpperCase() == "CHILD"){
                        childs1 = childs1 +1;
                    }
                }
                for(var k = 0;k < spouse[i].relationships2.length;k++)
                {
                    if(spouse[i].relationships2[k].relate.toUpperCase() == "CHILD"){
                        childs1 = childs1 +1;
                    }
                }
               
                if(childs1.length!==childs2.length)
                {
                    this.error.push("\nข้อมูลจำนวน บุตรไม่ตรงกัน");
                    this.error.push("\n"+person1.first_name+" "+person1.last_name+" มีจำนวนบุตร "+childs1);
                    this.error.push("\n"+person2.first_name+" "+person2.last_name+" มีจำนวนบุตร "+childs2);
                    //alert("isWrongMemberOfFamilyFatherMother");
                    result =true;
                    break;
                }
            }
            else {
                var person1 = this.GetDataById(spouse[i].id1);
                var person2 = this.GetDataById(spouse[i].id2);
                if(spouse[i].relationships1 == null){
                    if(person1.length>0){
                        this.error.push("\nไม่พบข้อมูลความสัมพันธ์ของ");
                        this.error.push("\n - คุณ "+person1[0].first_name+" "+person1[0].last_name);
                        result =true;
                    }
                }
                if(spouse[i].relationships2 == null){
                    if(person2.length>0){
                        this.error.push("\nไม่พบข้อมูลความสัมพันธ์ของ");
                        this.error.push("\n - คุณ "+person2[0].first_name+" "+person2[0].last_name);
                        result =true;
                    }
                }
                if(result){
                    break;
                }
            }
        }
      return result;
    }
    this.isNotHaveData = function(id){
        var result = false;
        var data = this.GetDataById(id);
        if(data.length>0){
            result = false;
        }
        else{
            this.error.push("\nไม่พบข้อมูลหมายเลข:"+id);
            result = true;
        }
    }
    this.GetDataById = function(id) {
        var result = [];
        for (var i = 0; i < this.data.length; i++) {
            if (this.data[i]["id"] == id) {
                result.push(this.data[i]);
                break;
            }
        }
        return result;
    }
    this.getErrorMessage = function(){
        return this.error.join(" ");
    }
    return this;
}
