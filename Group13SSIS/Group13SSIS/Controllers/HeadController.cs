using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Group13SSIS.Models;

namespace Group13SSIS.Controllers
{
    public class HeadController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }
        public ActionResult RequisitionList()
        {
            User user = (User)Session["user"];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var employeeids = db.Users.Where(x => x.DeptId == user.DeptId && x.RoleId == 2).Select(x => x.UserId).ToList();
                var requisitions = db.Requisitions.Where(x => employeeids.Contains(x.ApplicantId)).ToList();
                var requisitionlist = new List<RequisitionVM>();
                foreach (var item in requisitions)
                {
                    requisitionlist.Add(new RequisitionVM()
                    {
                        RequisitionId = item.RequisitionId,
                        ApplicantId = db.Users.Where(x => x.UserId == item.ApplicantId).FirstOrDefault().Name,
                        Date = item.Date,
                        Status = item.Status,
                        RejectReason = item.RejectReason
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
        public ActionResult ApproveRequisition(int id)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var re = db.Requisitions.Where(x => x.RequisitionId == id).FirstOrDefault();
                re.Status = "Approved";
                db.SaveChanges();
            }
            return RedirectToAction("RequisitionList");
        }
        [HttpGet]
        public ActionResult RejectRequisition(int id)
        {
            return View();
        }
        [HttpPost]
        public ActionResult RejectRequisition(int id, string reason)
        {
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var re = db.Requisitions.Where(x => x.RequisitionId == id).FirstOrDefault();
                re.Status = "Rejected";
                re.RejectReason = reason;
                db.SaveChanges();
            }
            return RedirectToAction("RequisitionList");
        }
        public ActionResult Setting()
        {
            User user = (User)Session["user"];
            using (Group13SSISEntities db = new Group13SSISEntities())
            {
                var dept = db.Depts.Where(x => x.DeptId == user.DeptId).FirstOrDefault();
                ViewData["rep"] = db.Users.Where(x => x.UserId == dept.RepId).FirstOrDefault();
                ViewData["cp"] = db.CollectionPoints.Where(x => x.PointId == dept.PointId).FirstOrDefault();
                ViewData["reps"] = db.Users.Where(x => x.DeptId == dept.DeptId && x.RoleId == 2).ToList();
                ViewData["cps"] = db.CollectionPoints.ToList();
            }
            return View();
        }
        public ActionResult SetRep(int? rep)
        {
            if(rep != null)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    User user = (User)Session["user"];
                    var dept = db.Depts.Where(x => x.DeptId == user.DeptId).FirstOrDefault();
                    dept.RepId = rep;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("Setting");
        }
        public ActionResult SetCp(int? cp)
        {
            if (cp != null)
            {
                using (Group13SSISEntities db = new Group13SSISEntities())
                {
                    User user = (User)Session["user"];
                    var dept = db.Depts.Where(x => x.DeptId == user.DeptId).FirstOrDefault();
                    dept.PointId = cp;
                    db.SaveChanges();
                }
            }
            return RedirectToAction("Setting");
        }
    }
}