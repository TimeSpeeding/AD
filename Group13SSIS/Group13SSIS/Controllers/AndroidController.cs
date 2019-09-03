using Group13SSIS.Models;
using Group13SSIS.Utility;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Group13SSIS.Controllers
{
    public class AndroidController : Controller
    {
        [HttpPost]
        public JsonResult setRepresentative(List<User> representativeList)
        {
            User representative = representativeList[0];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Dept dept = db.Depts.Where(d => d.Name == representative.Email).First();
                dept.RepId = db.Users.Where(u => u.Username == representative.Username).First().UserId;
                db.SaveChanges();
            }
            return null;
        }


        [HttpPost]
        public JsonResult getRepresentative()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var repre = db.Users
                    .Join(db.Depts, u => u.DeptId, d => d.DeptId, (u, d) => new
                    {
                        empName = u.Name,
                        depName = d.Name,
                        d.RepId
                    })
                    .Join(db.Users, u => u.RepId, uu => uu.UserId, (u, uu) => new
                    {
                        u.empName,
                        u.depName,
                        repreName = uu.Name
                    })
                    .ToList();
                return Json(repre);
            }
        }
        [HttpPost]
        public JsonResult GetRetrievalByDept()
        {
            Debug.WriteLine("GetRetrievalByDept");
            RetrievalVM vm = new RetrievalVM()
            {
                RetrievalDictionary = new Dictionary<string, Dictionary<int, RetrievalVM.Retrieval>>(),
                StationeryRetrievalDictionary = new Dictionary<int, RetrievalVM.Retrieval>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var retrievals = db.RequisitionDetails
                    .Where(rd => db.Requisitions.Where(r => r.Status == "Approved")
                                                .Select(r => r.RequisitionId)
                                                .ToList()
                                                .Contains(rd.RequisitionId))
                    .Join(db.Requisitions, rd => rd.RequisitionId, r => r.RequisitionId, (rd, r) => new
                    {
                        r.ApplicantId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Users, rd => rd.ApplicantId, u => u.UserId, (rd, u) => new
                    {
                        u.DeptId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Stationeries, rd => rd.StationeryId, s => s.StationeryId, (rd, s) => new
                    {
                        rd.DeptId,
                        rd.StationeryId,
                        s.Description,
                        rd.Quantity
                    }).ToList();

                var stockList = db.Stationeries.Select(s => new
                {
                    s.StationeryId,
                    s.Qty
                }).ToList();

                Dictionary<int, int> stock = new Dictionary<int, int>();
                foreach (var s in stockList)
                {
                    stock[s.StationeryId] = s.Qty;
                }

                foreach (var r in retrievals)
                {
                    String DeptName = db.Depts.Where(d => d.DeptId == r.DeptId).First().Name;
                    if (!vm.RetrievalDictionary.TryGetValue(DeptName, out Dictionary<int, RetrievalVM.Retrieval> retrievalList))
                        vm.RetrievalDictionary[DeptName] = new Dictionary<int, RetrievalVM.Retrieval>();
                    if (!vm.RetrievalDictionary[DeptName].TryGetValue(r.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = r.DeptId,
                            StationeryId = r.StationeryId,
                            Description = r.Description,
                            NeededQty = r.Quantity,
                        };
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].NeededQty += r.Quantity;
                    }

                    // automatically allocate
                    if (stock[r.StationeryId] >= r.Quantity)
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += r.Quantity;
                        stock[r.StationeryId] -= r.Quantity;
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += stock[r.StationeryId];
                        stock[r.StationeryId] = 0;
                    }

                }

                foreach (var s in retrievals)
                {
                    if (!vm.StationeryRetrievalDictionary.TryGetValue(s.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.StationeryRetrievalDictionary[s.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = s.DeptId,
                            StationeryId = s.StationeryId,
                            Description = s.Description,
                            NeededQty = s.Quantity,
                        };
                    }
                    else
                    {
                        retrieval.NeededQty += s.Quantity;
                    }
                }
                List<GetRetrievalByDeptModel> list = new List<GetRetrievalByDeptModel>();
                foreach (var dept in vm.RetrievalDictionary)
                {
                    string name = dept.Key;
                    foreach (var s in dept.Value)
                    {
                        list.Add(new GetRetrievalByDeptModel()
                        {
                            Name = name,
                            Description = s.Value.Description,
                            NeededQty = s.Value.NeededQty,
                            RetrievalQty = s.Value.RetrievalQty
                        });
                    }
                }
                return Json(list);
            }
        }

        public class GetRetrievalByDeptModel
        {
            public int DisbursementId { get; set; }
            public string Name { get; set; }
            public string Description { get; set; }
            public int NeededQty { get; set; }
            public int RetrievalQty { get; set; }
        }

        [HttpPost]
        public JsonResult GetRetrievalTotal()
        {
            Debug.WriteLine("GetRetrievalTotal");
            RetrievalVM vm = new RetrievalVM()
            {
                RetrievalDictionary = new Dictionary<string, Dictionary<int, RetrievalVM.Retrieval>>(),
                StationeryRetrievalDictionary = new Dictionary<int, RetrievalVM.Retrieval>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var retrievals = db.RequisitionDetails
                    .Where(rd => db.Requisitions.Where(r => r.Status == "Approved")
                                                .Select(r => r.RequisitionId)
                                                .ToList()
                                                .Contains(rd.RequisitionId))
                    .Join(db.Requisitions, rd => rd.RequisitionId, r => r.RequisitionId, (rd, r) => new
                    {
                        r.ApplicantId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Users, rd => rd.ApplicantId, u => u.UserId, (rd, u) => new
                    {
                        u.DeptId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Stationeries, rd => rd.StationeryId, s => s.StationeryId, (rd, s) => new
                    {
                        rd.DeptId,
                        rd.StationeryId,
                        s.Description,
                        rd.Quantity
                    }).ToList();

                var stockList = db.Stationeries.Select(s => new
                {
                    s.StationeryId,
                    s.Qty
                }).ToList();

                Dictionary<int, int> stock = new Dictionary<int, int>();
                foreach (var s in stockList)
                {
                    stock[s.StationeryId] = s.Qty;
                }

                foreach (var r in retrievals)
                {
                    String DeptName = db.Depts.Where(d => d.DeptId == r.DeptId).First().Name;
                    if (!vm.RetrievalDictionary.TryGetValue(DeptName, out Dictionary<int, RetrievalVM.Retrieval> retrievalList))
                        vm.RetrievalDictionary[DeptName] = new Dictionary<int, RetrievalVM.Retrieval>();
                    if (!vm.RetrievalDictionary[DeptName].TryGetValue(r.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = r.DeptId,
                            StationeryId = r.StationeryId,
                            Description = r.Description,
                            NeededQty = r.Quantity,
                        };
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].NeededQty += r.Quantity;
                    }

                    // automatically allocate
                    if (stock[r.StationeryId] >= r.Quantity)
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += r.Quantity;
                        stock[r.StationeryId] -= r.Quantity;
                    }
                    else
                    {
                        vm.RetrievalDictionary[DeptName][r.StationeryId].RetrievalQty += stock[r.StationeryId];
                        stock[r.StationeryId] = 0;
                    }

                }

                foreach (var s in retrievals)
                {
                    if (!vm.StationeryRetrievalDictionary.TryGetValue(s.StationeryId, out RetrievalVM.Retrieval retrieval))
                    {
                        vm.StationeryRetrievalDictionary[s.StationeryId] = new RetrievalVM.Retrieval()
                        {
                            DeptId = s.DeptId,
                            StationeryId = s.StationeryId,
                            Description = s.Description,
                            NeededQty = s.Quantity,
                        };
                    }
                    else
                    {
                        retrieval.NeededQty += s.Quantity;
                    }
                }
            }
            List<GetRetrievalTotalModel> list = new List<GetRetrievalTotalModel>();
            foreach (var s in vm.StationeryRetrievalDictionary)
            {
                int totalRetrievalQty = 0;
                foreach (var dept in vm.RetrievalDictionary)
                {
                    if (dept.Value.TryGetValue(s.Key, out RetrievalVM.Retrieval r))
                        totalRetrievalQty += r.RetrievalQty;
                }
                list.Add(new GetRetrievalTotalModel()
                {
                    stationeryName = s.Value.Description,
                    totalNeededQty = s.Value.NeededQty,
                    totalRetrievalQty = totalRetrievalQty
                });
            }

            return Json(list);
        }

        public class GetRetrievalTotalModel
        {
            public string stationeryName { get; set; }
            public int totalNeededQty { get; set; }
            public int totalRetrievalQty { get; set; }
        }


        [HttpPost]
        public void setCollectionPoint(List<Point> point)
        {
            Debug.WriteLine("setcollectionpoint");
            Debug.WriteLine(point[0].collectionPoint);
            Debug.WriteLine(point[0].dep);
            string deptName = point[0].dep;
            string collectionPoint = point[0].collectionPoint;

            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                Dept dept = db.Depts.Where(d => d.Name == deptName).First();
                CollectionPoint collection = db.CollectionPoints.Where(c => c.Name == collectionPoint).FirstOrDefault();
                dept.PointId = collection.PointId;
                db.SaveChanges();
            }
        }

        [HttpPost]
        public JsonResult getCollectionPoint()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var point = db.Users
                    .Join(db.Depts, u => u.DeptId, d => d.DeptId, (u, d) => new
                    {
                        empName = u.Name,
                        d.DeptId,
                        depName = d.Name,
                        d.PointId
                    })
                    .Join(db.CollectionPoints, u => u.PointId, c => c.PointId, (u, c) => new
                    {
                        u.empName,
                        u.DeptId,
                        u.depName,
                        collectionName = c.Name
                    })
                    .ToList();

                return Json(point);

            }
        }

        [HttpPost]
        public void UpdateRequestStatus(List<RequestStatus> requests)
        {
            RequestStatus request = requests[0];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {


                Requisition requestFound = db.Requisitions.Where(x => x.RequisitionId == request.RequisitionId).FirstOrDefault();
                requestFound.Status = request.Status;
                Debug.WriteLine("status" + requestFound.Status);
                db.SaveChanges();

            }
        }

        [HttpPost]
        public JsonResult GetRequest()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requests = db.Requisitions
                .Join(db.Users, r => r.ApplicantId, u => u.UserId, (r, u) => new
                {
                    r.RequisitionId,
                    r.Date,
                    u.Name,
                    u.DeptId,
                    r.Status,

                })
                .Join(db.Depts, r => r.DeptId, d => d.DeptId, (r, d) => new
                {
                    r.RequisitionId,
                    r.Date,
                    empName = r.Name,
                    depName = d.Name,
                    r.Status,
                }).ToList();

                List<requestModel> requestList = new List<requestModel>();
                foreach (var r in requests)
                {
                    requestList.Add(new requestModel
                    {
                        RequisitionId = r.RequisitionId,
                        Date = r.Date.ToShortDateString(),
                        depName = r.depName,
                        empName = r.empName,
                        Status = r.Status
                    });
                }

                return (Json(requestList));
            }
        }

        public class requestModel
        {
            public int RequisitionId { get; set; }
            public string Date { get; set; }
            public string empName { get; set; }
            public string depName { get; set; }
            public string Status { get; set; }
        }

        [HttpPost]
        public JsonResult GetRequestDetail()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requests = db.Requisitions
                    .Join(db.RequisitionDetails, r => r.RequisitionId, rd => rd.RequisitionId, (r, rd) => new
                    {
                        r.RequisitionId,
                        r.Date,
                        r.ApplicantId,
                        r.Status,
                        rd.RequisititonDetailId,
                        rd.StationeryId,
                        rd.Quantity
                    }).Join(db.Users, r => r.ApplicantId, u => u.UserId, (r, u) => new
                    {
                        r.RequisitionId,
                        r.Date,
                        u.Name,
                        u.DeptId,
                        r.Status,
                        r.RequisititonDetailId,
                        r.StationeryId,
                        r.Quantity
                    }).Join(db.Stationeries, r => r.StationeryId, s => s.StationeryId, (r, s) => new
                    {
                        r.RequisitionId,
                        r.Date,
                        r.Name,
                        r.DeptId,
                        r.Status,
                        r.RequisititonDetailId,
                        s.Description,
                        r.Quantity
                    }).Join(db.Depts, r => r.DeptId, d => d.DeptId, (r, d) => new
                    {
                        r.RequisitionId,
                        r.Date,
                        empName = r.Name,
                        depName = d.Name,
                        r.Status,
                        r.RequisititonDetailId,
                        r.Description,
                        r.Quantity
                    }).ToList();
                List<RequisitionModel> RequisitionList = new List<RequisitionModel>();
                foreach (var r in requests)
                {
                    RequisitionList.Add(new RequisitionModel()
                    {
                        RequisitionId = r.RequisitionId,
                        Date = r.Date.ToShortDateString(),
                        empName = r.empName,
                        depName = r.depName,
                        Description = r.Description,
                        RequisititonDetailId = r.RequisititonDetailId,
                        Quantity = r.Quantity,
                        Status = r.Status
                    });
                }

                return (Json(RequisitionList));
            }
        }
        public class RequisitionModel
        {
            public int RequisitionId { get; set; }
            public string Date { get; set; }

            public string empName { get; set; }

            public string depName { get; set; }

            public string Status { get; set; }

            public int RequisititonDetailId { get; set; }

            public string Description { get; set; }
            public int Quantity { get; set; }
        }

        [HttpPost]
        public void SetDisbursement(List<DisbursementModel> list)
        {
            Debug.WriteLine("SetDisbursement");
            DateTime now = DateTime.Now;
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requisitions = db.Requisitions.Where(x => x.Status == "Approved").ToList();
                foreach (var item in requisitions)
                {
                    item.Status = "Processed";
                }
                db.SaveChanges();
                Dictionary<string, int> deptDict = new Dictionary<string, int>();
                foreach (DisbursementModel d in list)
                {
                    int StationeryId = db.Stationeries.Where(s => s.Description == d.description).First().StationeryId;
                    int deptId = db.Depts.Where(dept => dept.Name == d.dep).First().DeptId;
                    int? pointId = db.Depts.Where(dept => dept.Name == d.dep).First().PointId;
                    int? repId = db.Depts.Where(dept => dept.Name == d.dep).First().RepId;
                    if (!deptDict.TryGetValue(d.dep, out int disbursementId))
                    {
                        Disbursement disbursement = new Disbursement()
                        {
                            Date = now,
                            DeptId = deptId,
                            PointId = pointId,
                            RepId = repId,
                            Status = "Retrieved"
                        };
                        db.Disbursements.Add(disbursement);
                        db.SaveChanges();
                        disbursementId = db.Disbursements.Where(dis => dis.Date == now.Date && dis.Status == "Retrieved" && dis.DeptId == deptId)
                            .OrderByDescending(dis => dis.DisbursementId)
                            .First().DisbursementId;
                        deptDict[d.dep] = disbursementId;
                    }
                    db.DisbursementDetails.Add(new DisbursementDetail()
                    {
                        DisbursementId = deptDict[d.dep],
                        StationeryId = StationeryId,
                        NeededQty = d.scheduledQty,
                        RetrievalQty = d.actualQty,
                    });
                    db.SaveChanges();
                }
            }
        }


        [HttpPost]
        public void UpdateDisbursement(List<DisbursementModel> list)
        {
            if (list == null)
                return;

            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (DisbursementModel d in list)
                {
                    int StationeryId = db.Stationeries.Where(s => s.Description == d.description).First().StationeryId;
                    var disbursementDetail = db.DisbursementDetails.Where(dd => dd.DisbursementId == d.id && dd.StationeryId == StationeryId).First();
                    disbursementDetail.DeliveryQty = d.deliveryQty;
                    db.SaveChanges();
                }
            }
        }

        //[HttpPost]
        //public void UpdateDisbursementStatus(List<Disbursement> list)
        //{
        //    using (Group13SSISEntities db = new Group13SSISEntities())
        //    {
        //        foreach (DisbursementModel d in list)
        //        {
        //            int StationeryId = db.Stationeries.Where(s => s.Description == d.description).First().StationeryId;
        //            var disbursementDetail = db.DisbursementDetails.Where(dd => dd.DisbursementId == d.id && dd.StationeryId == StationeryId).First();
        //            disbursementDetail.DeliveryQty = d.deliveryQty;
        //            db.SaveChanges();
        //        }
        //    }
        //}

        public class DisbursementModel
        {
            public int id { get; set; }
            public String description { get; set; }
            public String dep { get; set; }
            public int scheduledQty { get; set; }
            public int actualQty { get; set; }
            public int deliveryQty { get; set; }
        }

        [HttpPost]
        public JsonResult GetDisbursement()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var disbursements = db.DisbursementDetails.Where(dd => db.Disbursements
                .Where(d => d.Status == "Retrieved")
                .Select(d => d.DisbursementId)
                .ToList()
                .Contains(dd.DisbursementId))
                .Join(db.Disbursements, dd => dd.DisbursementId, d => d.DisbursementId, (dd, d) => new
                {
                    d.DeptId,
                    d.DisbursementId,
                    d.Date,
                    d.RepId,
                    dd.DisbursementDetailId,
                    dd.NeededQty,
                    dd.RetrievalQty,
                    dd.DeliveryQty,
                    dd.StationeryId
                }).Join(db.Depts, dd => dd.DeptId, dept => dept.DeptId, (dd, dept) => new
                {
                    dept.Name,
                    dd.DisbursementId,
                    dd.Date,
                    dd.RepId,
                    dd.DisbursementDetailId,
                    dd.NeededQty,
                    dd.RetrievalQty,
                    dd.DeliveryQty,
                    dd.StationeryId
                }).Join(db.Users, dd => dd.RepId, u => u.UserId, (dd, u) => new
                {
                    dd.Name,
                    dd.DisbursementId,
                    dd.Date,
                    u.UserId,
                    dd.DisbursementDetailId,
                    dd.NeededQty,
                    dd.RetrievalQty,
                    dd.DeliveryQty,
                    dd.StationeryId
                }).Join(db.Stationeries, dd => dd.StationeryId, s => s.StationeryId, (dd, s) => new
                {
                    dd.Name,
                    dd.DisbursementId,
                    dd.Date,
                    dd.UserId,
                    dd.DisbursementDetailId,
                    dd.NeededQty,
                    dd.RetrievalQty,
                    dd.DeliveryQty,
                    s.Description
                }).ToList();

                List<disbursementModel> list = new List<disbursementModel>();
                foreach (var d in disbursements)
                {
                    int? DeliveryQty = d.RetrievalQty;
                    if (d.DeliveryQty != null)
                        DeliveryQty = d.DeliveryQty;
                    list.Add(new disbursementModel()
                    {
                        Name = d.Name,
                        DisbursementId = d.DisbursementId,
                        Date = d.Date,
                        UserId = d.UserId,
                        DisbursementDetailId = d.DisbursementDetailId,
                        NeededQty = d.NeededQty,
                        RetrievalQty = d.RetrievalQty,
                        DeliveryQty = DeliveryQty,
                        Description = d.Description
                    });
                }
                return Json(list);
            }
        }

        [HttpPost]
        public JsonResult GetRep(List<User> userList)
        {
            List<GetRepModel> list = new List<GetRepModel>();
            if (userList == null)
            {
                list.Add(new GetRepModel()
                {
                    ifShow = false
                });
                return Json(list);
            }


            User user = userList[0];
            int userId = user.UserId;
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                List<int?> repIdList = db.Depts.Select(d => d.RepId).ToList();
                if (repIdList.Contains(userId))
                {
                    list.Add(new GetRepModel()
                    {
                        ifShow = true
                    });
                    return Json(list);
                }
                else
                {
                    list.Add(new GetRepModel()
                    {
                        ifShow = false
                    });
                    return Json(list);
                }
            }
        }

        public class GetRepModel
        {
            public bool ifShow { get; set; }
        }

        public class disbursementModel
        {
            public string Name { get; set; }
            public int DisbursementId { get; set; }
            public DateTime Date { get; set; }
            public int UserId { get; set; }
            public int DisbursementDetailId { get; set; }
            public int? NeededQty { get; set; }
            public int? RetrievalQty { get; set; }
            public int? DeliveryQty { get; set; }
            public string Description { get; set; }
        }

        [HttpPost]
        public JsonResult Login(List<User> users)
        {
            Debug.WriteLine("LOGIN");
            List<Object> status = new List<Object>();
            status.Add(new { context = "Login" });
            User user = null;
            if (users != null && users.Any())
            {
                user = users[0];
            }
            if (user != null)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    user.Password = Crypto.Hash(user.Password);
                    var userFound = db.Users.Where(x => x.Username == user.Username && x.Password == user.Password && x.Status == "Activated").FirstOrDefault();
                    if (userFound == null)
                    {
                        status.Add(new { status = "fail" });
                    }
                    else
                    {
                        status.Add(new
                        {
                            status = "OK",
                            role = db.Roles.Where(r => r.RoleId == userFound.RoleId).First().Name,
                            UserId = userFound.UserId,
                            depName = db.Depts.Where(d => d.DeptId == userFound.DeptId).First().Name,
                            UserName = db.Users.Where(u => u.UserId == userFound.UserId).First().Name
                        });
                    }
                }
            }
            else
            {
                status.Add(new { status = "fail" });
            }
            return Json(status);
        }

        [HttpPost]
        public JsonResult Logout()
        {
            List<Object> status = new List<Object>();
            status.Add(new { context = "Logout" });
            status.Add(new { status = "OK" });
            return Json(status);
        }

        [HttpPost]
        public JsonResult GetStationery()
        {
            List<Object> stationery = new List<Object>();
            stationery.Add(new { context = "GetStationery" });
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (var s in db.Stationeries.ToList())
                {
                    stationery.Add(s);
                }
            }
            return Json(stationery);
        }

        [HttpPost]
        public JsonResult PostRequisition(List<PostRequisitionModel> jsonList)
        {
            List<Object> status = new List<Object>();
            if (jsonList != null && jsonList.Any())
            {
                int UserId;
                if (jsonList[0] == null)
                    status.Add(new { status = "fail" });
                UserId = jsonList[0].UserId;
                jsonList.RemoveAt(0);
                DateTime now = DateTime.Now;
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    db.Requisitions.Add(new Requisition()
                    {
                        ApplicantId = UserId,
                        Date = now.Date,
                        Status = "Pending"
                    });
                    db.SaveChanges();
                    int requisitionId = db.Requisitions.Where(r => r.Date == now.Date && r.ApplicantId == UserId).OrderByDescending(r => r.RequisitionId).First().RequisitionId;
                    foreach (PostRequisitionModel x in jsonList)
                    {
                        if (x == null)
                            continue;
                        db.RequisitionDetails.Add(new RequisitionDetail
                        {
                            StationeryId = x.StationeryId,
                            Quantity = x.Qty,
                            RequisitionId = requisitionId
                        });
                        db.SaveChanges();
                    }
                }
                status.Add(new { context = "PostRequisition", status = "OK" });
            }
            else
            {
                status.Add(new { context = "PostRequisition", status = "fail" });
            }
            return Json(status);
        }

        [HttpPost]
        public JsonResult GetRequisition(List<GetRequisitionModel> jsonList)
        {
            List<Object> list = new List<Object>();
            if (jsonList == null)
                list.Add(new { context = "GetRequisition", status = "fail" });
            if (!jsonList.Any())
                list.Add(new { context = "GetRequisition", status = "fail" });

            GetRequisitionModel x = jsonList[0];
            int UserId = x.UserId;
            DateTime StartDate = DateTime.ParseExact(x.StartDate, "ddd MMM dd HH:mm:ss 'GMT'K yyyy", CultureInfo.InvariantCulture);
            DateTime EndDate = DateTime.ParseExact(x.EndDate, "ddd MMM dd HH:mm:ss 'GMT'K yyyy", CultureInfo.InvariantCulture);
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                List<Requisition> requisitions = db.Requisitions.Where(r => r.ApplicantId == UserId && r.Date.CompareTo(StartDate) >= 0 && r.Date.CompareTo(EndDate) <= 0).ToList();
                list.Add(new { context = "GetRequisition", status = "OK" });
                foreach (Requisition requisition in requisitions)
                {
                    list.Add(new
                    {
                        Date = requisition.Date,
                        RequisitionId = requisition.RequisitionId,
                        Status = requisition.Status,
                    });
                    //Debug.WriteLine(requisition.Date);
                    //Debug.WriteLine(requisition.RequisitionId);
                    //Debug.WriteLine(requisition.Status);
                }
            }
            return Json(list);
        }
        //[HttpPost]
        //public JsonResult GetRequisition2()
        //{
        //    List<Object> list = new List<Object>();
        //    using (Group13SSISEntities db = new Group13SSISEntities())
        //    {
        //        List<Requisition> requisitions = db.Requisitions.Join(db.Users )
        //    }
        //}

        [HttpPost]
        public JsonResult GetDepartment()
        {
            List<Object> list = new List<object>();
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                list.AddRange(db.Depts.ToList());
            }
            return Json(list);
        }


        public class GetRequisitionModel
        {
            public int UserId { get; set; }
            public string StartDate { get; set; }
            public string EndDate { get; set; }
        }

        public class PostRequisitionModel
        {
            public int UserId { get; set; }
            public int StationeryId { get; set; }
            public int Qty { get; set; }
        }

        public class RequestStatus
        {
            public int RequisitionId { get; set; }
            public string Status { get; set; }
        }

        public class Point
        {
            public string dep { get; set; }
            public string collectionPoint { get; set; }
        }


    }
}