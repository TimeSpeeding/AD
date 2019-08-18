using Group13SSIS.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Group13SSIS.Controllers
{
    public class ClerkController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }
        public ActionResult RequisitionList()
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requisitionlist = new List<RequisitionVM>();
                var requisitions = db.Requisitions.Where(x => x.Status == "Approved").ToList();
                foreach (var r in requisitions)
                {
                    requisitionlist.Add(new RequisitionVM()
                    {
                        RequisitionId = r.RequisitionId,
                        ApplicantId = db.Users.Where(x => x.UserId == r.ApplicantId).FirstOrDefault().Name,
                        Date = r.Date
                    });
                }
                ViewData["requisitionlist"] = requisitionlist;
            }
            return View();
        }

        public ActionResult RequisitionDetails(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var re = db.Requisitions.Where(x => x.RequisitionId == id).FirstOrDefault();
                RequisitionVM requisition = new RequisitionVM()
                {
                    RequisitionId = re.RequisitionId,
                    ApplicantId = db.Users.Where(x => x.UserId == re.ApplicantId).FirstOrDefault().Name,
                    Date = re.Date,
                    Status = re.Status,
                    RejectReason = re.RejectReason
                };
                ViewData["requisition"] = requisition;
                var details = db.RequisitionDetails.Where(x => x.RequisitionId == id).ToList();
                var list = new List<CartLine>();
                foreach (var item in details)
                {
                    CartLine cartLine = new CartLine()
                    {
                        Stationery = db.Stationeries.Where(x => x.StationeryId == item.StationeryId).FirstOrDefault(),
                        Qty = item.Quantity
                    };
                    list.Add(cartLine);
                }
                ViewData["list"] = list;
            }
            return View();
        }

        [HttpGet]
        public ActionResult RetrievalList()
        {
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
                            NeededQty = s.Quantity
                        };
                    }
                    else
                    {
                        retrieval.NeededQty += s.Quantity;
                    }
                }
            }

            return View(vm);
        }

        [HttpPost]
        public ActionResult RetrievalList(List<RetrievalVM.Retrieval> retrievalList)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var requisitions = db.Requisitions.Where(x => x.Status == "Approved").ToList();
                foreach(var item in requisitions)
                {
                    item.Status = "Delievered";
                }
                db.SaveChanges();
                Dictionary<int, int> deptDict = new Dictionary<int, int>();
                foreach (var r in retrievalList)
                {
                    if (!deptDict.TryGetValue(r.DeptId, out int disbursementId))
                    {
                        Dept dept = db.Depts.Where(x => x.DeptId == r.DeptId).FirstOrDefault();
                        DateTime now = DateTime.Now;
                        Disbursement d = new Disbursement()
                        {
                            DeptId = r.DeptId,
                            Date = now,
                            PointId = dept.PointId,
                            RepId = dept.RepId
                        };
                        db.Disbursements.Add(d);
                        db.SaveChanges();
                        int DisbursementId = db.Disbursements.Where(dbm => dbm.DeptId == d.DeptId && dbm.Date == now.Date)
                            .OrderByDescending(dbm => dbm.DisbursementId)
                            .First()
                            .DisbursementId;
                        deptDict[r.DeptId] = DisbursementId;
                    }
                    DisbursementDetail dd = new DisbursementDetail()
                    {
                        StationeryId = r.StationeryId,
                        DisbursementId = deptDict[r.DeptId],
                        NeededQty = r.NeededQty,
                        RetrievalQty = r.RetrievalQty,
                        DeliveryQty = r.RetrievalQty
                    };
                    db.DisbursementDetails.Add(dd);
                    var s = db.Stationeries.Where(x => x.StationeryId == r.StationeryId).FirstOrDefault();
                    s.Qty -= r.RetrievalQty;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("DeliveryList");
        }

        [HttpGet]
        public ActionResult DeliveryList()
        {
            DisbursementVM vm = new DisbursementVM()
            {
                Disbursements = new Dictionary<int?, List<DisbursementVM.Disbursement>>(),
                Depts = new Dictionary<int?, DisbursementVM.Dept>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var disbursementDetails = db.DisbursementDetails
                    .Join(db.Disbursements, dd => dd.DisbursementId, d => d.DisbursementId, (dd, d) => new
                    {
                        dd.DisbursementDetailId,
                        dd.DisbursementId,
                        d.DeptId,
                        d.PointId,
                        d.RepId,
                        dd.StationeryId,
                        dd.NeededQty,
                        dd.RetrievalQty,
                        dd.DeliveryQty,
                        dd.Comment
                    }).ToList();
                foreach (var dd in disbursementDetails)
                {
                    if (!vm.Disbursements.TryGetValue(dd.DeptId, out List<DisbursementVM.Disbursement> dList))
                    {
                        vm.Disbursements[dd.DeptId] = new List<DisbursementVM.Disbursement>();
                        vm.Depts[dd.DeptId] = new DisbursementVM.Dept()
                        {
                            DeptId = dd.DeptId,
                            DeptName = db.Depts.Where(d => d.DeptId == dd.DeptId).FirstOrDefault().Name,
                            Point = db.CollectionPoints.Where(cp => cp.PointId == dd.PointId).FirstOrDefault().Name,
                            RepName = db.Users.Where(u => u.UserId == dd.RepId).FirstOrDefault().Name
                        };
                    }
                    vm.Disbursements[dd.DeptId].Add(new DisbursementVM.Disbursement()
                    {
                        DisbursementDetailId = dd.DisbursementDetailId,
                        DeptId = dd.DeptId,
                        StationeryDescription = db.Stationeries.Where(s => s.StationeryId == dd.StationeryId).First().Description,
                        NeededQty = dd.NeededQty,
                        RetrievalQty = dd.RetrievalQty,
                        DeliveryQty = dd.DeliveryQty,
                        Comment = dd.Comment
                    });
                }
            }
            return View(vm);
        }
        [HttpPost]
        public ActionResult DeliveryList(List<DisbursementVM.Disbursement> dList)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                foreach (var d in dList)
                {
                    var DisbursementDetail = db.DisbursementDetails.Where(dd => dd.DisbursementDetailId == d.DisbursementDetailId).First();
                    DisbursementDetail.DeliveryQty = d.DeliveryQty;
                    DisbursementDetail.Comment = d.Comment;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("DeliveryList");
        }
        [HttpGet]
        public ActionResult Adjustment()
        {
            AdjustmentVM vm = new AdjustmentVM()
            {
                Stationeries = new Dictionary<string, List<Stationery>>()
            };
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var stationeries = db.Stationeries.Select(s => new
                {
                    s.StationeryId,
                    s.Category,
                    s.Description,
                    s.Qty,
                    s.UOM,
                }).ToList();
                foreach (var s in stationeries)
                {
                    if (!vm.Stationeries.TryGetValue(s.Category, out List<Stationery> sList))
                    {
                        vm.Stationeries[s.Category] = new List<Stationery>();
                    }

                    vm.Stationeries[s.Category].Add(new Stationery()
                    {
                        StationeryId = s.StationeryId,
                        Category = s.Category,
                        Description = s.Description,
                        Qty = s.Qty,
                        UOM = s.UOM
                    });
                }
            }
            return View(vm);
        }
        [HttpPost]
        public ActionResult Adjustment(List<AdjustmentDetail> ad)
        {
            var user = (User)Session["user"];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                DateTime now = DateTime.Now;
                db.Adjustments.Add(new Adjustment()
                {
                    ClerkId = user.UserId,
                    Date = now,
                    Status = "pending",
                });
                db.SaveChanges();
                Adjustment adjustment = db.Adjustments.Where(a => a.ClerkId == user.UserId && a.Date == now.Date).OrderByDescending(a => a.AdjustmentId).First();
                double total = 0.0;
                foreach (var item in ad)
                {
                    db.AdjustmentDetails.Add(new AdjustmentDetail()
                    {
                        AdjustmentId = adjustment.AdjustmentId,
                        StationeryId = item.StationeryId,
                        Qty = item.Qty,
                        Reason = item.Reason
                    });
                    db.SaveChanges();
                    total += Math.Abs(db.Stationeries.Where(s => s.StationeryId == item.StationeryId).First().Price * item.Qty);
                }
                adjustment.Amount = total;
                db.SaveChanges();
            }
            return RedirectToAction("Adjustment");
        }
    }
}