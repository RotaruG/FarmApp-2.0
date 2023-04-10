const config = require('./env.json')[process.env.STAGE || 'prod'];
const SNOWFLAKE = require('snowflake-sdk');
let ion = require("ion-js");
const to = require("await-to-js").default;
const aws = require("aws-sdk");


var connection = SNOWFLAKE.createConnection({
  account: config.account,
  username: config.username,
  password: config.password,
  role: config.role,
  warehouse: config.warehouse,
  clientSessionKeepAlive: false
});

const documentClient = new aws.DynamoDB.DocumentClient();


module.exports.index = async () => {

  // connection.connect(
  //   function (err, conn) {
  //     if (err) {
  //       console.error('Unable to connect: ' + err.message);
  //     }
  //     else {
  //       console.log('Successfully connected to Snowflake.');
  //     }
  //   }
  // );

  // //#region TEST QUERIES
  //   var statement = connection.execute({
  // sqlText:'select * from construction_raw.ibt.BranchInspectionModels' ,
  //    complete: function (err, stmt, rows) {
  //       if (err) {
  //         console.error('Failed to execute statement due to the following error: ' + err.message);
  //       } else {
  //         console.log('Successfully executed statement: ', rows);
  //       }
  //     }
  //   });

    

  // var statement = connection.execute({
  //   sqlText: 'CREATE TABLE construction_raw.ibt.BranchInspectionModels ("eqID" VARCHAR(819200), "additionalNotes" VARCHAR(819200), "branch" VARCHAR(819200),"createdAt" VARCHAR(819200), "description" VARCHAR(819200), "eqCategory" VARCHAR(819200), "questionSet" VARIANT, "recurrencyValue" VARCHAR(819200))' , 
  //   complete: function (err, stmt, rows) {
  //     if (err) {
  //       console.error('Failed to execute statement due to the following error: ' + err.message);
  //     } else {
  //       console.log('Successfully executed statement: ',rows);
  //     }
  //   }
  // });

  // var statement = connection.execute({
  //   sqlText: 'CREATE TABLE construction_raw.ibt.BranchInspectionModelsTemp ("eqID" VARCHAR(819200), "additionalNotes" VARCHAR(819200), "branch" VARCHAR(819200),"createdAt" VARCHAR(819200), "description" VARCHAR(819200), "eqCategory" VARCHAR(819200), "questionSet" VARCHAR(819200), "recurrencyValue" VARCHAR(819200))' , 
  //   complete: function (err, stmt, rows) {
  //     if (err) {
  //       console.error('Failed to execute statement due to the following error: ' + err.message);
  //     } else {
  //       console.log('Successfully executed statement: ',rows);
  //     }
  //   }
  // });

  // var statement = connection.execute({
  //   sqlText: 'truncate table construction_raw.ibt.BranchInspectionsTemp' , 
  //   complete: function (err, stmt, rows) {
  //     if (err) {
  //       console.error('Failed to execute statement due to the following error: ' + err.message);
  //     } else {
  //       console.log('Successfully executed statement: ',rows);
  //     }
  //   }
  // });

  // var statement = connection.execute({
  //   sqlText: 'truncate table construction_raw.ibt.BranchInspections' , 
  //   complete: function (err, stmt, rows) {
  //     if (err) {
  //       console.error('Failed to execute statement due to the following error: ' + err.message);
  //     } else {
  //       console.log('Successfully executed statement: ',rows);
  //     }
  //   }
  // });

  //#endregion

  //test();

  //All necessary functions are called in this function to avoid asynchronous call

  let con = await checkConnection();
  console.log(con);
  if (con) {
    let first = await branchInspectionsProcess();
    console.log(first);
    let second = await branchInspectionsModelsProcess();
    console.log(second);
    let third = await terminateConnection();
    console.log(third);
  }






}

//#region Main function 

//#endregion

//#region Main data processing functions

async function branchInspectionsProcess(tableName) {

  return new Promise(async (resolve) => {
    const params = {
      TableName: "BranchInspections",
      Select: "COUNT",
    };
    const dynamoDbCount = await documentClient.scan(params).promise();
    var countValue = 0;
    var statement = connection.execute({
      sqlText: "select COUNT (*) from construction_raw.ibt.BranchInspections",
      complete: async function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          for (let item in rows[0]) {
            countValue = rows[0][item];
            console.log("BranchInspection count value = ", rows[0][item]);
            console.log("DynamoDb Count = ", dynamoDbCount.Count);

            if (countValue === dynamoDbCount.Count) {
              console.log("Snowflake is up to date! (" + countValue + " - " + dynamoDbCount.Count + ")");
              resolve("Snowflake is up to date! (" + countValue + " - " + dynamoDbCount.Count + ")");
            } else {
              console.log("Snowflake is behind IBT, updating!");
              console.log("Extracting data from dynamoDB");
              var dynamoDbdata = await scanTable("BranchInspections");
              var scheduledData = await scanTable("RecurringSchedule");

              if (dynamoDbdata) {

                console.log("Truncated tables in snowflake and inserting data succes")
                await truncateBranchInspectionsTables().then(async (res) => await simplifiedInspectionsProcess(dynamoDbdata, scheduledData).then(async (res) => await insertIntoBranchInspectionsTemp(dynamoDbdata).then(async (res) => await insertIntoBranchInspections())));
                resolve("Truncated tables in snowflake and inserting data succes");
              }


            }

          }
        }
      }
    });
  })
}

async function branchInspectionsModelsProcess() {

  return new Promise(async (resolve) => {
    const params = {
      TableName: "BranchInspectionModels",
      Select: "COUNT",
    };
    const dynamoDbCount = await documentClient.scan(params).promise();
    var countValue = 0;
    var statement = connection.execute({
      sqlText: "select COUNT (*) from construction_raw.ibt.BranchInspectionModels",
      complete: async function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          for (let item in rows[0]) {
            countValue = rows[0][item];
            console.log("BranchInspectionModels count value = ", rows[0][item]);
            console.log("DynamoDb Count = ", dynamoDbCount.Count);

            if (countValue === dynamoDbCount.Count) {
              console.log("Snowflake is up to date! (" + countValue + " - " + dynamoDbCount.Count + ")");
              resolve("Snowflake is up to date! (" + countValue + " - " + dynamoDbCount.Count + ")");
            } else {
              console.log("Snowflake is behind IBT, updating!");
              console.log("Extracting data from dynamoDB");
              var dynamoDbdata = await scanTable("BranchInspectionModels");

              if (dynamoDbdata) {

                console.log("Truncated tables in snowflake")
                await truncateBranchInspectionModelsTables();


                console.log("Inserting in snowflake table");
                await insertIntoBranchInspectionModelsTemp(dynamoDbdata).then(async (res) => await insertIntoBranchInspectionModels());

                resolve("Inserted BranchInspectionModels");

              }


            }

          }
        }
      }
    });
  })
}
//#endregion

//#region INSERT functions used in main functions

async function insertIntoInspectionTemp(params) {
  return new Promise(async (resolve) => {
    console.log("insertIntoInspectionTemp");


    const arrayToInsert = [];


    for (let index = 0; index < params.length; index++) {
      const element = params[index];

      const tempArray = [];
      tempArray.push(element.userId.toString());
      tempArray.push(element.noteId.toString());
      tempArray.push(element.additionalNotes.toString());
      tempArray.push(element.branch.toString());
      tempArray.push(element.certifier.toString());
      tempArray.push(element.completeBoolean.toString());
      tempArray.push(element.createdAt.toString());
      tempArray.push(element.date.toString());
      tempArray.push(element.dayAtMidPst.toString());
      tempArray.push(element.description.toString());
      tempArray.push(element.eqCategory.toString());
      tempArray.push(element.eqID.toString());
      tempArray.push(JSON.stringify(element.extraNotes));
      tempArray.push(JSON.stringify(element.inspection));
      tempArray.push(element.lockTagOut.toString());
      tempArray.push(element.openIssues.toString());
      tempArray.push(JSON.stringify(element.openIssuesMap));
      tempArray.push(element.otherNotes.toString());
      tempArray.push(element.sprayRadio.toString());
      tempArray.push(element.taskDuration.toString());
      tempArray.push(element.userName.toString());
      if (element.recurrencyValue) {
        tempArray.push(element.recurrencyValue.toString());
      } else {
        tempArray.push("");
      }


      arrayToInsert.push(tempArray);

    }


    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.InspectionsTemp ("userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" , "createdAt", "date", "dayAtMidPst", "description", "eqCategory", "eqID", "extraNotes", "inspection", "lockTagOut", "openIssues", "openIssuesMap", "otherNotes", "sprayRadio", "taskDuration", "userName", "recurrencyValue") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);',
      binds: arrayToInsert,
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.InspectionsTemp: ' + stmt.getSqlText());
          resolve();
        }
      }
    });


  })

}

async function insertIntoInspections() {
  return new Promise(async (resolve) => {

    console.log("insertIntoInspections");

    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.Inspections ("userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" , "createdAt", "date", "dayAtMidPst", "description", "eqCategory", "eqID", "extraNotes", "inspection", "lockTagOut", "openIssues", "openIssuesMap", "otherNotes", "sprayRadio", "taskDuration", "userName", "recurrencyValue") SELECT "userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" , "createdAt", "date", "dayAtMidPst", "description", "eqCategory", "eqID", parse_json("extraNotes"), parse_json("inspection"), "lockTagOut", "openIssues", parse_json("openIssuesMap"), "otherNotes", "sprayRadio", "taskDuration", "userName", "recurrencyValue" FROM construction_raw.ibt.InspectionsTemp',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.Inspections: ' + stmt.getSqlText());
          resolve();
        }
      }
    });


  })

}

async function insertIntoBranchInspectionModels() {
  return new Promise(async (resolve) => {

    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.BranchInspectionModels ("eqID" , "additionalNotes" , "branch" ,"createdAt" , "description" , "eqCategory" , "questionSet","recurrencyValue") SELECT "eqID" , "additionalNotes" , "branch" ,"createdAt" , "description" , "eqCategory" , parse_json("questionSet"),"recurrencyValue" FROM construction_raw.ibt.BranchInspectionModelsTemp;',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.BranchInspectionModels: ' + stmt.getSqlText());
        }
      }
    });

  })


}

async function insertIntoBranchInspectionModelsTemp(params) {
  return new Promise(async (resolve) => {

    console.log("insertIntoBranchInspectionModelsTemp");

    const arrayToInsert = [];
    var scheduledData = await scanTable("RecurringSchedule");

    for (let index = 0; index < params.length; index++) {
      const element = params[index];

      const tempArray = [];
      tempArray.push(element.eqID.toString());
      if (element.additionalNotes != undefined) {
        tempArray.push(element.additionalNotes.toString());
      } else {
        tempArray.push("");
      }

      tempArray.push(element.branch.toString());
      tempArray.push(element.createdAt.toString());
      tempArray.push(element.description.toString());
      tempArray.push(element.eqCategory.toString());
      tempArray.push(JSON.stringify(element.questionSet));

      for (let index = 0; index < scheduledData.length; index++) {
        const value = scheduledData[index];

        if (value.eqID.toString() === element.eqID.toString()) {

          tempArray.push(convertDayWeekString(value.dayWeekCount.toString())); 
  
        }
        
      }

      arrayToInsert.push(tempArray);

    }

    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.BranchInspectionModelsTemp ("eqID" , "additionalNotes" , "branch" ,"createdAt" , "description" , "eqCategory" , "questionSet" ,"recurrencyValue") values (?,?,?,?,?,?,?,?);',
      binds: arrayToInsert,
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.BranchInspectionModelsTemp : ' + stmt.getSqlText());
          resolve();
        }
      }
    });


  })

}

async function insertIntoBranchInspections() {

  return new Promise((resolve) => {

    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.BranchInspections ("userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" ,"createdAt" , "date" , "dayAtMidPst" ,"description" , "eqCategory" , "eqID" ,"extraNotes" , "inspectionInfo" , "lockTagOut" ,"openIssues" , "openIssuesMap" , "otherNotes" ,"sprayRadio" , "taskDuration", "userName") SELECT "userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" ,"createdAt" , "date" , "dayAtMidPst" ,"description" , "eqCategory" , "eqID" ,parse_json("extraNotes") , parse_json("inspectionInfo") , "lockTagOut" ,"openIssues" , parse_json("openIssuesMap") , "otherNotes" ,"sprayRadio" , "taskDuration", "userName" from construction_raw.ibt.BranchInspectionsTemp; ',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.BranchInspections: ' + stmt.getSqlText());
          resolve();
        }
      }
    });

  })



}

async function insertIntoBranchInspectionsTemp(params) {
  return new Promise(async (resolve) => {

    const arrayToInsert = [];


    for (let index = 0; index < params.length; index++) {
      const element = params[index];
      const tempArray = [];
      tempArray.push(element.userId);
      tempArray.push(element.noteId);
      if (element.additionalNotes !== undefined) {
        tempArray.push(element.additionalNotes);
      } else {
        tempArray.push("");
      }

      tempArray.push(element.branch);
      tempArray.push(element.certifier);
      tempArray.push(element.completeBoolean);
      tempArray.push(element.createdAt);
      tempArray.push(element.date);
      tempArray.push(element.dayAtMidPst);
      tempArray.push(element.description);
      tempArray.push(element.eqCategory);
      tempArray.push(element.eqID);
      tempArray.push(JSON.stringify(element.extraNotes));
      tempArray.push(JSON.stringify(element.inspectionInfo));
      tempArray.push(element.lockTagOut);
      tempArray.push(element.openIssues);
      tempArray.push(JSON.stringify(element.openIssuesMap));
      tempArray.push(element.otherNotes);
      tempArray.push(element.sprayRadio);
      tempArray.push(element.taskDuration);
      tempArray.push(element.userName);

      arrayToInsert.push(tempArray);
    }



    var statement = connection.execute({
      sqlText: 'insert into construction_raw.ibt.BranchInspectionsTemp ("userId" , "noteId" , "additionalNotes" ,"branch" , "certifier" , "completeBoolean" ,"createdAt" , "date" , "dayAtMidPst" ,"description" , "eqCategory" , "eqID" ,"extraNotes" , "inspectionInfo" , "lockTagOut" ,"openIssues" , "openIssuesMap" , "otherNotes" ,"sprayRadio" , "taskDuration", "userName") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ',
      binds: arrayToInsert,
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement insert into construction_raw.ibt.BranchInspectionsTemp: ' + stmt.getSqlText());
          resolve();
        }
      }
    });


  })

}
//#endregion

//#region DataBase Utility Functions
async function scanTable(tableName) {

  const params = {
    TableName: tableName,
  };

  const scanResults = [];
  var items;
  do {
    items = await documentClient.scan(params).promise();
    items.Items.forEach((item) => scanResults.push(item));
    params.ExclusiveStartKey = items.LastEvaluatedKey;
  } while (typeof items.LastEvaluatedKey !== "undefined");


  return scanResults;




}


async function checkConnection() {
  return new Promise(async (resolve) => {
    if (connection.isUp()) {
      console.log("Connected");
      resolve(true);
    } else {
      connection = SNOWFLAKE.createConnection({
        account: config.account,
        username: config.username,
        password: config.password,
        role: config.role,
        warehouse: config.warehouse,
        clientSessionKeepAlive: false
      });
      connection.connect(
        function (err, conn) {
          if (err) {
            console.error('Unable to connect: ' + err.message);
          }
          else {
            console.log('Successfully connected to Snowflake.');
            resolve(true);
          }
        }
      );
    }
  })
}

async function terminateConnection() {
  return new Promise(async (resolve) => {
    connection.destroy(function (err, conn) {
      if (err) {
        console.error('Unable to disconnect: ' + err.message);
      } else {
        console.log('Disconnected connection with id: ' + connection.getId());
        resolve('Disconnected connection with id: ' + connection.getId());
      }
    });

  })
}



async function truncateBranchInspectionsTables() {
  return new Promise(async (resolve) => {
    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.BranchInspectionsTemp',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.BranchInspectionsTemp: ', rows);
        }
      }
    });

    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.BranchInspections',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.BranchInspections: ', rows);
        }
      }
    });

    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.Inspections',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error truncate table construction_raw.ibt.Inspections: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.Inspections: ', rows);
        }
      }
    });

    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.InspectionsTemp',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.InspectionsTemp : ', rows);
        }
      }
    });

    resolve();
  })
}

async function truncateBranchInspectionModelsTables() {
  return new Promise(async (resolve) => {
    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.BranchInspectionModelsTemp',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.BranchInspectionModelsTemp: ', rows);
        }
      }
    });

    connection.execute({
      sqlText: 'truncate table construction_raw.ibt.BranchInspectionModels',
      complete: function (err, stmt, rows) {
        if (err) {
          console.error('Failed to execute statement due to the following error: ' + err.message);
        } else {
          console.log('Successfully executed statement truncate table construction_raw.ibt.BranchInspectionModels: ', rows);
        }
      }
    });

    resolve();
  })
}
//#endregion

//#region Utility Functions
function convertDayWeekString(dayWeekString) {
  const dayArray = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday"
  ];
  const weekArray = [
    "Week One",
    "Week Two",
    "Week Three",
    "Week Four"
  ];
  const monthArray = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  ];
  const recurrencyArray = [
    "Monthly",
    "6 Months",
    "3 Months",
    "Annually"
  ];

  //Will split it into two strings the first one is for the branch object and the second is the index for the inspection
  //console.log("dayWeekString",dayWeekString)
  const schedInfo = dayWeekString.split("-");
  //console.log("schedInfo",schedInfo)

  const dayValue = schedInfo[0];
  const weekValue = schedInfo[1];
  const monthValue = schedInfo[2];
  const recurrencyValue = schedInfo[3];

  //we substract 1 because on ScheduleInspection the dropdowns contain "0" as first option - "Default Value"
  const convertDay = dayArray[parseInt(dayValue) - 1];
  const convertWeek = weekArray[parseInt(weekValue) - 1];
  const convertMonth = monthArray[parseInt(monthValue) - 1];
  const convertRecurrency = recurrencyArray[parseInt(recurrencyValue) - 1];

  let convertedString = "";
  if (schedInfo.length == 4 && convertRecurrency != "Annually") {
    convertedString = `Every: ${convertRecurrency}, ${convertDay}, ${convertWeek}, ${convertMonth}`;
  } else if (schedInfo.length == 4 && convertRecurrency === "Annually") {
    convertedString = `${convertRecurrency}, ${convertDay}, ${convertWeek}, ${convertMonth}`;
  } else {
    convertedString = `Every: ${convertDay}, ${convertWeek}`;
  }

  return convertedString;
}
//#endregion


async function simplifiedInspectionsProcess(data, scheduledData) {

  return new Promise(async (resolve) => {

    //console.log(data[0]);

    var finalArray = [];


    for (let index = 0; index < data.length; index++) {
      const value = data[index];
      let arrayTemp = [];

      var finalObject = {};
      finalObject.userId = value.userId;
      finalObject.noteId = value.noteId;

      if (value.additionalNotes) {
        finalObject.additionalNotes = value.additionalNotes;
      } else {
        finalObject.additionalNotes = "";
      }

      finalObject.branch = value.branch;

      finalObject.certifier = value.certifier;

      finalObject.completeBoolean = value.completeBoolean;

      finalObject.createdAt = value.createdAt;

      finalObject.date = value.date;

      finalObject.dayAtMidPst = value.dayAtMidPst;

      finalObject.description = value.description;

      finalObject.eqCategory = value.eqCategory;

      finalObject.eqID = value.eqID;

      finalObject.extraNotes = value.extraNotes;
      var listOfInspectionObjects = [];

      for (let index = 0; index < value.inspectionInfo.length; index++) {
        const element = value.inspectionInfo[index];
        //console.log("name ",element.name);
        //console.log("list ",element.qList);
        var inspectionListObject = {};


        var questionArray = [];
        var valueArray = [];
        var commentArray = [];
        var photoArray = [];
        var photoTwoArray = [];
        var fixedByArray = [];
        for (let index2 = 0; index2 < element.qList.length; index2++) {
          const value2 = element.qList[index2];

          var questionObject = {};
          var valueObj = {};
          var commentObj = {};
          var photoObj = {};
          var photoTwoObj = {};
          var fixedByObj = {};


          if (value2.qText) {
            questionObject.QuestionText = value2.qText.toString() + "(" + index2 + ")";
            questionArray.push(questionObject);
          }
          if (value2.value) {
            valueObj.Value = value2.value.toString() + "(" + index2 + ")";
            valueArray.push(valueObj);
          }
          if (value2.comment) {
            commentObj.Comment = value2.comment.toString() + "(" + index2 + ")";
            commentArray.push(commentObj);
          }
          if (value2.photo) {
            photoObj.Photo = value2.photo.toString() + "(" + index2 + ")";
            photoArray.push(photoObj);
          }
          if (value2.photoTwo) {
            photoTwoObj.PhotoTwo = value2.photoTwo.toString() + "(" + index2 + ")";
            photoTwoArray.push(photoTwoObj);
          }
          if (value2.FixedBy) {
            fixedByObj.FixedBy = value2.FixedBy.toString() + "(" + index2 + ")";
            fixedByArray.push(fixedByObj);
          }

        }

        inspectionListObject.QuestionList = questionArray;
        inspectionListObject.ValueList = valueArray;
        inspectionListObject.CommentList = commentArray;
        inspectionListObject.Photo = photoArray;
        inspectionListObject.PhotoTwo = photoTwoArray;
        inspectionListObject.FixedBy = fixedByArray;



        var inspection = {
          name: element.name,
          inspectionList: inspectionListObject
        };

        listOfInspectionObjects.push(inspection);


        // finalIbaniiString = element.name.toString() + " : " + ibaniiString;


      }
      finalObject.inspection = listOfInspectionObjects;

      finalObject.lockTagOut = value.lockTagOut;

      finalObject.openIssues = value.openIssues;


      var openIssuesObject = {};


      var questionArray = [];
      var valueArray = [];
      var commentArray = [];
      var photoArray = [];
      var photoTwoArray = [];
      var fixedByArray = [];

      for (let index = 0; index < value.openIssuesMap.length; index++) {
        const element = value.openIssuesMap[index];

        let qListHuev = value.inspectionInfo[element.cateIndex].qList[element.questionIndex];

        var questionObject = {};
        var valueObj = {};
        var commentObj = {};
        var photoObj = {};
        var photoTwoObj = {};
        var fixedByObj = {};

        if (qListHuev.qText) {
          questionObject.QuestionText = qListHuev.qText.toString();
          questionArray.push(questionObject);

        }
        if (qListHuev.value != undefined) {
          valueObj.Value = qListHuev.value.toString();
          valueArray.push(valueObj);
        }
        if (qListHuev.comment != undefined) {
          commentObj.Comment = qListHuev.comment.toString();
          commentArray.push(commentObj);
        }
        if (qListHuev.photo != undefined) {
          photoObj.Photo = qListHuev.photo.toString();
          photoArray.push(photoObj);
        }
        if (qListHuev.photoTwo != undefined) {
          photoTwoObj.PhotoTwo = qListHuev.photoTwo.toString();
          photoTwoArray.push(photoTwoObj);
        }
        if (qListHuev.FixedBy != undefined) {
          fixedByObj.FixedBy = qListHuev.FixedBy.toString();
          fixedByArray.push(fixedByObj);
        }


      }

      openIssuesObject.QuestionList = questionArray;
      openIssuesObject.ValueList = valueArray;
      openIssuesObject.CommentList = commentArray;
      openIssuesObject.Photo = photoArray;
      openIssuesObject.PhotoTwo = photoTwoArray;
      openIssuesObject.FixedBy = fixedByArray;
      finalObject.openIssuesMap = openIssuesObject

      finalObject.otherNotes = value.otherNotes;

      finalObject.sprayRadio = value.sprayRadio;
      finalObject.taskDuration = value.taskDuration;
      finalObject.userName = value.userName;


      for (let index = 0; index < scheduledData.length; index++) {
        const element = scheduledData[index];

        if (element.eqID.toString() === value.eqID.toString()) {

          finalObject.recurrencyValue = convertDayWeekString(element.dayWeekCount.toString());

          break;

        }


      }


      finalArray.push(finalObject);
      // arrayTemp2.push(arrayTemp);


    }

    //insert in snowflake temp and main table


    console.log("Inserting in snowflake table");
    await insertIntoInspectionTemp(finalArray).then(async (res) => await insertIntoInspections());


    resolve();

  })




}
